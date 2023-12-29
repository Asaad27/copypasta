package com.asaad27.repository

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.asaad27.model.ClipboardModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AndroidSystemClipboardRepository(context: Context): ISystemClipboardRepository {

    private val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val clipboardChannel = Channel<ClipboardModel>(Channel.CONFLATED)

    init {
        clipboard.addPrimaryClipChangedListener {
            val newContent = getCurrentContent()
            newContent?.let { clipboardChannel.trySend(it) }
        }
    }

    override fun getCurrentContent(): ClipboardModel? {
        val clipData = clipboard.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val item = clipData.getItemAt(0)
            val text = item.text.toString()
            return ClipboardModel(fullContent = text)
        }
        return null
    }

    override fun setCurrentContent(content: ClipboardModel) {
        val clip = ClipData.newPlainText("CopyPasta", content.fullContent)
        clipboard.setPrimaryClip(clip)
    }

    override fun clipboardFlow(): Flow<ClipboardModel> = clipboardChannel.receiveAsFlow()

    override fun pasteContent() {
        TODO("instead of pasting, show a notification to user to paste manually")
    }
}