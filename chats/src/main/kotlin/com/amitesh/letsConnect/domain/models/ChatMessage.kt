package com.amitesh.letsConnect.domain.models

import com.amitesh.letsConnect.domain.type.ChatId
import com.amitesh.letsConnect.domain.type.ChatMessageId
import java.time.Instant

data class ChatMessage(
    val id: ChatMessageId,
    val chatId: ChatId,
    val sender: ChatParticipant,
    val content: String,
    val createdAt: Instant
)
