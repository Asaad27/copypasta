package com.asaad27.di

import com.asaad27.repository.AndroidSystemClipboardRepository
import com.asaad27.repository.IClipboardRepository
import com.asaad27.repository.ISystemClipboardRepository
import com.asaad27.repository.JvmInMemoryClipboardRepository
import com.asaad27.service.AndroidSystemClipboardService
import com.asaad27.service.IClipboardService
import com.asaad27.service.ISystemClipboardService
import com.asaad27.service.JvmClipboardService
import com.asaad27.ui.viewmodel.ClipboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidAppModule = module {

    single<IClipboardRepository> { JvmInMemoryClipboardRepository() }
    single<ISystemClipboardRepository> { AndroidSystemClipboardRepository(androidContext()) }

    single<ISystemClipboardService> { AndroidSystemClipboardService(get<ISystemClipboardRepository>()) }
    single<IClipboardService> { JvmClipboardService(get<IClipboardRepository>()) }
    single<ClipboardViewModel> {
        ClipboardViewModel(
            viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            systemClipboard = get(),
            clipboardService = get()
        )
    }
}