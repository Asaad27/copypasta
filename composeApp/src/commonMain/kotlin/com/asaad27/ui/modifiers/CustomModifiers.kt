package com.asaad27.ui.modifiers

import androidx.compose.ui.Modifier

expect fun Modifier.customPointerInput(
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onEnter: () -> Unit,
    onExit: () -> Unit
): Modifier