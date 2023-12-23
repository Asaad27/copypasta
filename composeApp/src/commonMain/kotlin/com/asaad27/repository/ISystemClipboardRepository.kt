package com.asaad27.repository

import com.asaad27.model.ClipboardModel
import kotlinx.coroutines.flow.Flow

interface ISystemClipboardRepository {
    fun getCurrentContent(): ClipboardModel?
    fun setCurrentContent(content: ClipboardModel)
    fun clipboardFlow(): Flow<ClipboardModel>
    fun pasteContent()
}