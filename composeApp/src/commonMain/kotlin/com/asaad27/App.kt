package com.asaad27

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.asaad27.model.ClipboardModel
import com.asaad27.ui.components.ClipboardItemCard
import com.asaad27.ui.components.IconData
import com.asaad27.ui.components.SearchBarComponent
import com.asaad27.ui.theme.CommonTheme

@Composable
fun App() {
    CommonTheme(useDarkTheme = true) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Bottom app bar",
                    )
                }
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {

                ClipboardList(
                    clipboardItems = listOf(
                        ClipboardModel(14, "Hello"),
                        ClipboardModel(15, "Hello again"),
                    ),
                    focusedItemIndex = null,
                    lazyListState = LazyListState(),
                )

                SearchBarComponent(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    focusRequester = FocusRequester(),
                    searchText = "",
                    onExit = {},
                    onSearchTextChanged = {}
                )

            }
        }
    }
}

@Composable
fun ClipboardList(
    modifier: Modifier = Modifier,
    clipboardItems: List<ClipboardModel>,
    focusedItemIndex: Int?,
    lazyListState: LazyListState,
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(clipboardItems) { index, item ->
            ClipboardItemCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .requiredHeight(150.dp),
                item = item,
                isFocused = focusedItemIndex == index,
                onItemClicked = { println("clicked") },
                icons = listOf(
                    IconData(
                        icon = Icons.Default.Add,
                        description = "Add",
                        onClick = {
                            println("Add")
                        }
                    ),
                    IconData(
                        icon = Icons.Default.Edit,
                        description = "Edit",
                        onClick = {}
                    ),
                    IconData(
                        icon = Icons.Default.Delete,
                        description = "Delete",
                        onClick = {}
                    ),
                )
            ) {
                Text(
                    text = item.preview.repeat(1000),
                    modifier = Modifier
                        .padding(top = 24.dp, start = 8.dp, end = 10.dp, bottom = 4.dp)
                )
            }
        }
    }
}
