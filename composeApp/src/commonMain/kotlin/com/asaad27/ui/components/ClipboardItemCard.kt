package com.asaad27.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.asaad27.ui.modifiers.customPointerInput
import com.asaad27.ui.theme.CardElevation

@Composable
fun <T> ClipboardItemCard(
    modifier: Modifier = Modifier,
    item: T,
    isFocused: Boolean = false,
    onItemClicked: (T) -> Unit,
    icons: List<IconData<T>> = listOf(),
    content: @Composable () -> Unit
) {
    val isHovered = remember { mutableStateOf(false) }
    val isPressed = remember { mutableStateOf(false) }

    ElevatedCard(
        colors = determineCardColors(isPressed.value, isHovered.value, isFocused),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = CardElevation.default,
            pressedElevation = CardElevation.pressed,
            focusedElevation = CardElevation.focused,
            hoveredElevation = CardElevation.hovered,
            disabledElevation = CardElevation.disabled
        ),
        modifier = modifier
            .clickable(onClick = { onItemClicked(item) })
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                    onItemClicked(item)
                    true
                } else {
                    false
                }
            }
            .pointerHoverIcon(PointerIcon.Hand)
            .customPointerInput(
                onEnter = { isHovered.value = true },
                onExit = { isHovered.value = false },
                onPress = { isPressed.value = true },
                onRelease = { isPressed.value = false }
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content()

            if (icons.isNotEmpty()) {
                Row(
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    icons.map { iconData ->
                        Icon(
                            imageVector = iconData.icon,
                            contentDescription = iconData.description,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable(onClick = { iconData.onClick(item) })
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun determineCardColors(
    isPressed: Boolean,
    isHovered: Boolean,
    isFocused: Boolean
): CardColors {
    val containerColor = when {
        isPressed -> MaterialTheme.colorScheme.secondary
        isHovered -> MaterialTheme.colorScheme.secondaryContainer
        isFocused -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val contentColor = when {
        isPressed -> MaterialTheme.colorScheme.onSecondary
        isHovered -> MaterialTheme.colorScheme.onSecondaryContainer
        isFocused -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    return CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor)
}

data class IconData<T>(
    val icon: ImageVector,
    val description: String,
    val onClick: (T) -> Unit
)