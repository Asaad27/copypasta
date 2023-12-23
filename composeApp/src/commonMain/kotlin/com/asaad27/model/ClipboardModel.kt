package com.asaad27.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class ClipboardModel(
    val id: Int? = null,
    val fullContent: String = "",
    var lastUpdated: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClipboardModel) return false
        return fullContent.equals(other.fullContent, ignoreCase = true)
    }

    override fun hashCode(): Int {
        return fullContent.lowercase().hashCode()
    }
}