package com.asaad27.services

import com.asaad27.model.ClipboardModel
import kotlinx.coroutines.flow.Flow

interface IClipboardService {
    suspend fun saveClipboardContent(content: ClipboardModel): ClipboardModel
    suspend fun getAllClipboardContents(): List<ClipboardModel>
    suspend fun searchClipboardContents(query: String): Flow<ClipboardModel>
    suspend fun getByContent(fullContent: String): ClipboardModel?
}