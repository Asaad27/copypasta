package com.asaad27.service

import com.asaad27.model.ClipboardModel
import com.asaad27.repository.ISystemClipboardRepository


class DesktopSystemClipboardService(private val systemClipboardRepository: ISystemClipboardRepository) :
    ISystemClipboardService {

    override fun getCurrentContent(): ClipboardModel? =
        systemClipboardRepository.getCurrentContent()

    override fun setCurrentContent(content: ClipboardModel) =
        systemClipboardRepository.setCurrentContent(content)

    override fun clipboardFlow() = systemClipboardRepository.clipboardFlow()
    override fun pasteContent() = systemClipboardRepository.pasteContent()
}