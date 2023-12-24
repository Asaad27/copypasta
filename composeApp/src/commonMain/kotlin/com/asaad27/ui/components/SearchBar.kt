package com.asaad27.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val debouncePeriod = 500L

@Composable
fun SearchBarComponent(
    focusRequester: FocusRequester,
    label: String = "Search",
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
) {
    val isFocusCaptured = remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchText,
            label = { Text(label) },
            singleLine = true,
            maxLines = 1,
            onValueChange = {
                searchText = it
                debounceJob?.cancel()
                debounceJob = coroutineScope.launch {
                    delay(debouncePeriod)
                    onSearchTextChanged(it)
                }
                captureFocusIfNotCaptured(focusRequester, isFocusCaptured.value)
            },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .focusRequester(focusRequester)
                .textFieldKeyEvents(onExit.also {
                    isFocusCaptured.value = false
                }),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                disabledIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

private fun captureFocusIfNotCaptured(
    focusRequester: FocusRequester,
    isFocusCaptured: Boolean
) {
    if (!isFocusCaptured) {
        focusRequester.captureFocus()
    }
}

@Composable
private fun Modifier.textFieldKeyEvents(onExit: () -> Unit) = this
    .onKeyEvent {
        when (it.key) {
            Key.Enter, Key.DirectionUp, Key.DirectionDown -> {
                onExit()
                true
            }

            else -> false
        }
    }