package com.asaad27.repository


import com.asaad27.adapter.extractText
import com.asaad27.model.ClipboardModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.event.KeyEvent
import java.util.Locale

class DesktopSystemClipboardRepository : ClipboardOwner, ISystemClipboardRepository {

    private val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    private val clipboardChannel = Channel<ClipboardModel>(Channel.CONFLATED)
    private val robot = java.awt.Robot()
    private val ctrlKey =
        if (System.getProperty("os.name").lowercase(Locale.getDefault())
                .contains("mac")
        ) KeyEvent.VK_META else KeyEvent.VK_CONTROL

    init {
        takeOwnership()
    }

    override fun clipboardFlow(): Flow<ClipboardModel> = flow {
        for (content in clipboardChannel) {
            emit(content)
        }
    }

    override fun pasteContent() {
        robot.keyPress(ctrlKey)
        robot.keyPress(KeyEvent.VK_V)
        robot.keyRelease(KeyEvent.VK_V)
        robot.keyRelease(ctrlKey)
    }

    override fun lostOwnership(clipboard: Clipboard, contents: Transferable) {
        takeOwnership()
        val newContent = getCurrentContent()
        newContent?.let { clipboardChannel.trySend(it) }
    }

    override fun getCurrentContent(): ClipboardModel? {
        return try {
            val textContent = clipboard.extractText()
            if (textContent.isEmpty()) return null
            ClipboardModel(fullContent = textContent)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun setCurrentContent(content: ClipboardModel) {
        val stringSelection = StringSelection(content.fullContent)
        clipboard.setContents(stringSelection, this)
    }

    private fun takeOwnership() {
        repeat(3) {
            try {
                val transferable = clipboard.getContents(this)
                clipboard.setContents(transferable, this)
                return
            } catch (e: IllegalStateException) {
                Thread.sleep(500)
            }
        }
    }
}