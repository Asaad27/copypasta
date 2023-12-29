package com.asaad27.service

import com.asaad27.model.ClipboardModel
import com.asaad27.repository.IClipboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class JvmClipboardService(
    private val clipboardRepository: IClipboardRepository
) : IClipboardService {
    override suspend fun saveClipboardContent(content: ClipboardModel): ClipboardModel {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        content.lastUpdated = now
        return clipboardRepository.save(content)
    }

    override suspend fun getAllClipboardContents(): List<ClipboardModel> {
        return clipboardRepository.getAll()
    }

    override fun searchClipboardContents(query: String): Flow<ClipboardModel> {
        return clipboardRepository.search(query)
    }

    override suspend fun getByContent(fullContent: String): ClipboardModel? {
        return clipboardRepository.getClipboardContentByContent(fullContent)
    }
}