package com.asaad27.ui.modifiers

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.customPointerInput(
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onEnter: () -> Unit,
    onExit: () -> Unit
): Modifier = this.onPointerEvent(PointerEventType.Enter) { onEnter() }
    .onPointerEvent(PointerEventType.Exit) { onExit() }
    .onPointerEvent(PointerEventType.Press) { onPress() }
    .onPointerEvent(PointerEventType.Release) { onRelease() }