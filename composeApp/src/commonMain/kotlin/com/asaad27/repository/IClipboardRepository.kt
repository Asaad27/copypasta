package com.asaad27.repository

import com.asaad27.model.ClipboardModel
import kotlinx.coroutines.flow.Flow

interface IClipboardRepository {
    suspend fun save(content: ClipboardModel): ClipboardModel
    suspend fun getAll(): List<ClipboardModel>
    fun search(query: String): Flow<ClipboardModel>
    suspend fun getClipboardContentByContent(fullContent: String): ClipboardModel?
}

interface IDatabaseConnector {
     fun connect()
}
