package com.asaad27.adapter

import com.asaad27.model.ClipboardModel
import com.asaad27.repository.database.ClipboardEntity

fun ClipboardEntity.toClipboardModel(): ClipboardModel {
    val entity = this
    return ClipboardModel(
        id = this.id.value,
        fullContent = entity.content,
    )
}


