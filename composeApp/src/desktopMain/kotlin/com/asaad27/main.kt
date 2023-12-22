package com.asaad27

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.asaad27.App
import java.awt.Window

fun main() = application {
    //val shouldMinimize by viewModel.shouldMinimize.collectAsState()

    val windowState = rememberWindowState(width = 400.dp, height = 1000.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "CopyPasta",
        state = windowState,
    ) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}