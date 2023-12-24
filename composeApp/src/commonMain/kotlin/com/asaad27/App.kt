package com.asaad27

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.asaad27.ui.components.ClipboardScreen
import com.asaad27.ui.theme.CommonTheme
import com.asaad27.ui.viewmodel.ClipboardViewModel

@Composable
fun App(
    viewModel: ClipboardViewModel
) {
    CommonTheme {
        Scaffold(
            bottomBar = { BottomBar() }
        ) { paddingValues ->
            ClipboardScreen(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(56.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Bottom app bar",
        )
    }
}

