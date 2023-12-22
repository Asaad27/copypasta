package com.asaad27.ui.modifiers

import androidx.compose.ui.Modifier

actual fun Modifier.customPointerInput(
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onEnter: () -> Unit,
    onExit: () -> Unit
): Modifier = this
