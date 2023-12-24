package com.asaad27.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.asaad27.model.ClipboardModel

private val iconDataList = listOf<IconData<ClipboardModel>>(
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

@Composable
fun ClipboardList(
    modifier: Modifier = Modifier,
    clipboardItems: List<ClipboardModel>,
    focusedItemIndex: Int?,
    lazyListState: LazyListState,
    onItemClicked: (ClipboardModel) -> Unit
) {
    val clipboardItemModifier = Modifier
        .padding(horizontal = 16.dp)
        .clip(MaterialTheme.shapes.medium)
        .requiredHeight(150.dp)

    val clipboardContentModifier = Modifier
        .padding(top = 24.dp, start = 8.dp, end = 10.dp, bottom = 4.dp)

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(clipboardItems) { index, item ->
            ClipboardItemCard(
                modifier = clipboardItemModifier,
                item = item,
                isFocused = focusedItemIndex == index,
                onItemClicked = { onItemClicked(it) },
                isScrollable = true,
                icons = iconDataList
            ) {
                Text(
                    text = item.fullContent,
                    modifier = clipboardContentModifier
                )
            }
        }
    }
}