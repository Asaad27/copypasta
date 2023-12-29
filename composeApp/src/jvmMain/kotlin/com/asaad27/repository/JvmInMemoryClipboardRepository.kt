package com.asaad27.repository

import com.asaad27.model.ClipboardModel
import com.asaad27.utils.md5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.TreeSet

class JvmInMemoryClipboardRepository: IClipboardRepository {


    private val clipboardHistory = TreeSet(
        compareBy<ClipboardModel> {
            it.lastUpdated
        }.thenBy {
            it.fullContent.md5()
        }
    )

    override suspend fun save(content: ClipboardModel): ClipboardModel {
        clipboardHistory.add(content)
        return content
    }

    override suspend fun getAll(): List<ClipboardModel> {
        return clipboardHistory.toList().sortedBy {
            it.lastUpdated
        }
    }

    override fun search(query: String): Flow<ClipboardModel> {
        return clipboardHistory.filter {
            it.fullContent.contains(query, ignoreCase = true)
        }.asFlow()
    }

    override suspend fun getClipboardContentByContent(fullContent: String): ClipboardModel? {
        return clipboardHistory.firstOrNull {
            it.fullContent.equals(fullContent, ignoreCase = true)
        }
    }
}