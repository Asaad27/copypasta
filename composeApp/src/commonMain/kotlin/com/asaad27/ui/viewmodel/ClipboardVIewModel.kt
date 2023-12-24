package com.asaad27.ui.viewmodel

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.asaad27.model.ClipboardModel
import com.asaad27.services.IClipboardService
import com.asaad27.services.ISystemClipboardService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class ClipboardViewModel(
    viewModelScope: CoroutineScope,
    private val systemClipboard: ISystemClipboardService,
    private val clipboardService: IClipboardService
) : ViewModel(viewModelScope) {

    private val _uiState = MutableStateFlow(ClipboardContentScreenState())
    val uiState = _uiState.asStateFlow()

    private val _firstVisibleItemIndex = MutableStateFlow(0)

    private val _isFloatingDownButtonVisible = MutableStateFlow(false)
    val isFloatingDownButtonVisible = _isFloatingDownButtonVisible.asStateFlow()

    private val _shouldMinimize = MutableStateFlow(false)
    val shouldMinimize = _shouldMinimize.asStateFlow()

    private var originalClipboardContents = listOf<ClipboardModel>()

    private var filteredClipboardContents = listOf<ClipboardModel>()
        set(value) {
            field = value
            updateClipboardContents(value)
            updateFocusedElement(null)
        }

    private var isSearching: Boolean = false
    private var searchJob: Job? = null
    private val searchDebounceTime = 700L

    init {
        viewModelScope.launch {
            monitorClipboard()
        }

        viewModelScope.launch {
            clipboardService.getAllClipboardContents().let {
                originalClipboardContents = it
                filteredClipboardContents = it.toList()
            }
        }

        viewModelScope.launch {
            combine(
                _uiState.map { it.clipboardContents.size },
                _firstVisibleItemIndex
            ) { totalItems, currentFirstVisibleIndex ->
                totalItems >= 8 && currentFirstVisibleIndex < totalItems - 8
            }.collect { isVisible ->
                _isFloatingDownButtonVisible.value = isVisible
            }
        }
    }

    private fun monitorClipboard() {
        viewModelScope.launch {
            systemClipboard.clipboardFlow().collect { systemClipboardContent ->
                val content =
                    clipboardService.getByContent(systemClipboardContent.fullContent) ?: systemClipboardContent

                clipboardService.saveClipboardContent(content).also {
                    originalClipboardContents += it
                    if (!isSearching) {
                        filteredClipboardContents += it
                    }
                }
            }
        }
    }

    override fun clear() {
        super.clear()
        searchJob?.cancel()
    }

    fun onSearchClipboardContent(value: String) {
        _uiState.value = _uiState.value.copy(searchText = value)
        searchJob?.cancel()
        isSearching = value.isNotEmpty()

        if (value.isEmpty()) {
            updateClipboardContents(originalClipboardContents)
        } else {
            searchJob = viewModelScope.launch {
                delay(searchDebounceTime)
                var isFirstElement = true
                clipboardService.searchClipboardContents(value).collect {
                    if (isFirstElement) {
                       filteredClipboardContents = emptyList()
                        isFirstElement = false
                    }
                    filteredClipboardContents += it
                }
            }
        }

    }

    fun onItemClicked(item: ClipboardModel) {
        viewModelScope.launch {
            systemClipboard.setCurrentContent(item)
            pasteContent()
        }
    }

    fun onWindowMinimized() {
        _shouldMinimize.value = false
    }

    fun onScrollToLastItem() {
        val index = _uiState.value.clipboardContents.size - 1
        onScrollToItem(index)
    }

    private fun onScrollToItem(index: Int) {
        updateFocusedElement(index)
    }

    fun onSearchExit() {
        handleKeyAction(Key.DirectionUp)
    }

    fun onKeyEvent(keyEvent: KeyEvent): Boolean {
        if (keyEvent.type != KeyEventType.KeyDown) return false
        handleKeyAction(keyEvent.key)
        return true
    }

    fun updateFirstVisibleItemIndex(index: Int) {
        println("updateFirstVisibleItemIndex: $index")
        _firstVisibleItemIndex.value = index
    }

    private fun handleKeyAction(key: Key) {
        val currentState = _uiState.value
        when (key) {
            Key.DirectionDown -> updateFocusedElement(
                minOf((currentState.focusedIndex ?: -1) + 1, currentState.clipboardContents.size - 1)
            )
            Key.DirectionUp -> updateFocusedElement(
                maxOf((currentState.focusedIndex ?: currentState.clipboardContents.size) - 1, 0)
            )
            Key.Enter -> currentState.focusedIndex?.let {
                onItemClicked(currentState.clipboardContents[it])
            }
        }
    }

    private fun pasteContent() {
        viewModelScope.launch {
            _shouldMinimize.value = true
            delay(500)
            systemClipboard.pasteContent()
            onWindowMinimized()
        }
    }

    private fun updateClipboardContents(contents: List<ClipboardModel>) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(clipboardContents = contents)
    }

    private fun updateFocusedElement(index: Int?) {
        val currentState = _uiState.value
        val indexToScrollTo = index ?: maxOf(0, (currentState.clipboardContents.size - 1))
        _uiState.value = currentState.copy(focusedIndex = index, indexToScrollTo = indexToScrollTo)
    }

}

data class ClipboardContentScreenState(
    val focusedIndex: Int? = null,
    val copiedItemIndex: Int? = null,
    val searchText: String = "",
    val indexToScrollTo: Int? = null,
    val clipboardContents: List<ClipboardModel> = emptyList(),
)