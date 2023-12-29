package com.asaad27

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.asaad27.di.androidAppModule
import com.asaad27.ui.viewmodel.ClipboardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(androidAppModule)
        }

        val viewModel: ClipboardViewModel by KoinJavaComponent.inject(ClipboardViewModel::class.java)

        setContent {
            App(viewModel)
        }
    }
}