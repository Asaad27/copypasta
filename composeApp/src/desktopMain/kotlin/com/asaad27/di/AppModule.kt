package com.asaad27.di

import com.asaad27.repository.IClipboardRepository
import com.asaad27.repository.IDatabaseConnector
import com.asaad27.repository.ISystemClipboardRepository
import com.asaad27.repository.JvmClipboardRepository
import com.asaad27.repository.DesktopSystemClipboardRepository
import com.asaad27.repository.database.JvmDatabaseConnector
import com.asaad27.services.DesktopSystemClipboardService
import com.asaad27.services.IClipboardService
import com.asaad27.services.ISystemClipboardService
import com.asaad27.services.JvmClipboardService
import com.asaad27.ui.viewmodel.ClipboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {
    single<IDatabaseConnector> { JvmDatabaseConnector() }

    single<IClipboardRepository> { JvmClipboardRepository(get<IDatabaseConnector>()) }
    single<ISystemClipboardRepository> { DesktopSystemClipboardRepository() }

    single<ISystemClipboardService> { DesktopSystemClipboardService(get<ISystemClipboardRepository>()) }
    single<IClipboardService> { JvmClipboardService(get<IClipboardRepository>()) }
    single<ClipboardViewModel> {
        ClipboardViewModel(
            viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            systemClipboard = get(),
            clipboardService = get()
        )
    }
}