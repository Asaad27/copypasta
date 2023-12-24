package com.asaad27.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import com.asaad27.ui.viewmodel.ClipboardViewModel
import kotlinx.coroutines.launch

@Composable
fun ClipboardScreen(
    modifier: Modifier = Modifier,
    viewModel: ClipboardViewModel
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val isFloatingDownButtonVisible by viewModel.isFloatingDownButtonVisible.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val listFocusRequester = remember { FocusRequester() }
    val searchBarFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.focusedIndex) {
        listFocusRequester.requestFocus()
    }

    LaunchedEffect(uiState.indexToScrollTo) {
        scope.launch {
            uiState.indexToScrollTo?.let {
                lazyListState.animateScrollToItem(it)
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { index ->
                viewModel.updateFirstVisibleItemIndex(index)
            }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            ClipboardList(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(listFocusRequester)
                    .focusable()
                    .onKeyEvent(viewModel::onKeyEvent),
                clipboardItems = uiState.clipboardContents,
                focusedItemIndex = uiState.focusedIndex,
                lazyListState = lazyListState,
                onItemClicked = viewModel::onItemClicked
            )

            SearchBarComponent(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                focusRequester = searchBarFocusRequester,
                onExit = {
                    searchBarFocusRequester.freeFocus()
                    listFocusRequester.requestFocus()
                    viewModel.onSearchExit()
                },
                onSearchTextChanged = viewModel::onSearchClipboardContent
            )
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = isFloatingDownButtonVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        viewModel.onScrollToLastItem()
                        uiState.indexToScrollTo?.let {
                            lazyListState.animateScrollToItem(it)
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Scroll to Bottom"
                )
            }
        }
    }
}