package com.asaad27.services

import com.asaad27.model.ClipboardModel
import kotlinx.coroutines.flow.Flow

interface ISystemClipboardService {
    fun getCurrentContent(): ClipboardModel?
    fun setCurrentContent(content: ClipboardModel)
    fun clipboardFlow(): Flow<ClipboardModel>
    fun pasteContent()
}