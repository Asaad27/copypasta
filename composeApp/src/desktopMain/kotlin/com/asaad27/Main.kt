package com.asaad27

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.asaad27.di.appModule
import com.asaad27.ui.viewmodel.ClipboardViewModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent

fun main() = application {
    startKoin {
        modules(appModule)
    }

    val viewModel: ClipboardViewModel by KoinJavaComponent.inject(ClipboardViewModel::class.java)
    val shouldMinimize by viewModel.shouldMinimize.collectAsState()
    val windowState = rememberWindowState(width = 400.dp, height = 1000.dp)
    Window(
        onCloseRequest = {
            viewModel.clear()
            exitApplication()
        },
        title = "CopyPasta",
        state = windowState,
        visible = true,
        alwaysOnTop = true,
    ) {
        if (shouldMinimize) {
            windowState.isMinimized = true
            viewModel.onWindowMinimized()
        }

        App(viewModel)
    }

}