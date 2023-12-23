package com.asaad27.ui.viewmodel

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.asaad27.model.ClipboardModel
import com.asaad27.services.IClipboardService
import com.asaad27.services.ISystemClipboardService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ClipboardViewModel(
    viewModelScope: CoroutineScope,
    private val systemClipboard: ISystemClipboardService,
    private val clipboardService: IClipboardService
) : ViewModel(viewModelScope) {
    //private val logger = LoggerFactory.getLogger(javaClass)

    private val _uiState = MutableStateFlow(ClipboardContentScreenState())
    val uiState = _uiState.asStateFlow()

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
    }

    private suspend fun monitorClipboard() {
        systemClipboard.clipboardFlow().collect { systemClipboardContent ->
            val content =
                clipboardService.getByContent(systemClipboardContent.fullContent) ?: systemClipboardContent

            clipboardService.saveClipboardContent(content).also {
                //logger.debug("saved: {}", it.preview)
                originalClipboardContents += it
                if (!isSearching) {
                    filteredClipboardContents += it
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
                clipboardService.searchClipboardContents(value).collect {
                    filteredClipboardContents += it
                }
            }
        }

    }

    fun onItemClicked(item: ClipboardModel) {
        //logger.debug("onItemClicked: {}", item)
        viewModelScope.launch {
            systemClipboard.setCurrentContent(item)
            pasteContent()
        }
    }

    fun onWindowMinimized() {
        _shouldMinimize.value = false
    }

    fun onScrollToItem(index: Int) {
        updateFocusedElement(index)
    }

    fun onKeyEvent(keyEvent: androidx.compose.ui.input.key.KeyEvent): Boolean {
        if (keyEvent.type != KeyEventType.KeyDown) {
            return false
        }

        val currentState = _uiState.value
        if (keyEvent.key == Key.Enter) {
            _uiState.value.focusedIndex?.let {
                onItemClicked(currentState.clipboardContents[it])
            }
            return true
        }

        val clipboardSize = currentState.clipboardContents.size
        val newFocusedIndex = when (keyEvent.key) {
            Key.DirectionDown -> minOf((currentState.focusedIndex ?: -1) + 1, clipboardSize - 1)
            Key.DirectionUp -> maxOf((currentState.focusedIndex ?: clipboardSize) - 1, 0)
            else -> null
        }

        if (newFocusedIndex != null) {
            updateFocusedElement(newFocusedIndex)
            return true
        }

        return false
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
        val element = index ?: maxOf(0, (currentState.clipboardContents.size - 1))
        _uiState.value = currentState.copy(focusedIndex = element, indexToScrollTo = element)
    }

}

data class ClipboardContentScreenState(
    val focusedIndex: Int? = null,
    val copiedItemIndex: Int? = null,
    val searchText: String = "",
    val indexToScrollTo: Int? = null,
    val clipboardContents: List<ClipboardModel> = emptyList(),
)