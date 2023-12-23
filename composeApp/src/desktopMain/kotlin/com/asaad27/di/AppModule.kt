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
import org.koin.dsl.module

val appModule = module {
   single<IDatabaseConnector> { JvmDatabaseConnector() }

    single<IClipboardRepository> { JvmClipboardRepository(get()) }
    single<ISystemClipboardRepository> { DesktopSystemClipboardRepository() }

    single<ISystemClipboardService> { DesktopSystemClipboardService(get()) }
    single<IClipboardService> { JvmClipboardService(get()) }
    //single<ClipboardViewModel> { ClipboardViewModel(get(), get()) }
}