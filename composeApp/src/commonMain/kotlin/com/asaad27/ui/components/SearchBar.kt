package com.asaad27.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarComponent(
    focusRequester: FocusRequester,
    searchText: String,
    modifier: Modifier = Modifier,
    onExit: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchText,
            label = { Text("Search") },
            onValueChange = {
                onSearchTextChanged(it)
                focusRequester.captureFocus()
            },

            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .focusRequester(focusRequester)
                .onKeyEvent {
                    if (it.key == Key.Enter || it.key == Key.DirectionUp || it.key == Key.DirectionDown) {
                        onExit()
                    }
                    false
                },
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