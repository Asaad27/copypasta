package com.asaad27.ui.viewmodel

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.asaad27.model.ClipboardModel
import com.asaad27.service.IClipboardService
import com.asaad27.service.ISystemClipboardService
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

    private var originalClipboardContents = mutableListOf<ClipboardModel>()
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            loadInitialClipboardContents()
            monitorClipboard()
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

    fun onSearchClipboardContent(value: String) {
        val isSearching = value.isNotEmpty()
        _uiState.value = _uiState.value.copy(searchText = value, isSearching = isSearching)
        searchJob?.cancel()

        if (!isSearching) {
            updateClipboardContents(originalClipboardContents)
        } else {
            searchJob = viewModelScope.launch {
                updateClipboardContents(emptyList())
                clipboardService.searchClipboardContents(value).collect { searchResult ->
                    updateClipboardContents(_uiState.value.clipboardContents + searchResult)
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
        val lastIndex = _uiState.value.clipboardContents.size - 1
        updateFocusedElement(lastIndex)
    }

    fun onSearchExit() {
        handleKeyAction(Key.DirectionUp)
    }

    fun onKeyEvent(keyEvent: KeyEvent): Boolean {
        if (keyEvent.type == KeyEventType.KeyDown) {
            handleKeyAction(keyEvent.key)
            return true
        }
        return false
    }

    fun updateFirstVisibleItemIndex(index: Int) {
        _firstVisibleItemIndex.value = index
    }

    private fun handleKeyAction(key: Key) {
        val focusedIndex = _uiState.value.focusedIndex
        val clipboardSize = _uiState.value.clipboardContents.size

        when (key) {
            Key.DirectionDown -> updateFocusedElement(minOf((focusedIndex ?: -1) + 1, clipboardSize - 1))
            Key.DirectionUp -> updateFocusedElement(maxOf((focusedIndex ?: clipboardSize) - 1, 0))
            Key.Enter -> focusedIndex?.let { onItemClicked(_uiState.value.clipboardContents[it]) }
        }
    }

    private suspend fun loadInitialClipboardContents() {
        clipboardService.getAllClipboardContents().let { clipboardContents ->
            originalClipboardContents.addAll(clipboardContents)
            updateClipboardContents(clipboardContents)
        }
    }

    private suspend fun monitorClipboard() {
        systemClipboard.clipboardFlow().collect { systemClipboardContent ->
            val content =
                clipboardService.getByContent(systemClipboardContent.fullContent) ?: systemClipboardContent

            clipboardService.saveClipboardContent(content).also {
                originalClipboardContents.add(it)
                if (!uiState.value.isSearching) {
                    updateClipboardContents(originalClipboardContents)
                }
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
        _uiState.value = _uiState.value.copy(clipboardContents = contents)
    }

    private fun updateFocusedElement(index: Int) {
        _uiState.value = _uiState.value.copy(focusedIndex = index, indexToScrollTo = index)
    }
}

data class ClipboardContentScreenState(
    val focusedIndex: Int? = null,
    val searchText: String = "",
    val isSearching: Boolean = false,
    val indexToScrollTo: Int? = null,
    val clipboardContents: List<ClipboardModel> = emptyList()
)
