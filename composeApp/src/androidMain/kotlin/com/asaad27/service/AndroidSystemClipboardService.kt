package com.asaad27.service

import com.asaad27.model.ClipboardModel
import com.asaad27.repository.ISystemClipboardRepository
import kotlinx.coroutines.flow.Flow

class AndroidSystemClipboardService(private val systemClipboardRepository: ISystemClipboardRepository): ISystemClipboardService {
    override fun getCurrentContent(): ClipboardModel? = systemClipboardRepository.getCurrentContent()

    override fun setCurrentContent(content: ClipboardModel) = systemClipboardRepository.setCurrentContent(content)

    override fun clipboardFlow(): Flow<ClipboardModel> = systemClipboardRepository.clipboardFlow()
    override fun pasteContent() = systemClipboardRepository.pasteContent()
}