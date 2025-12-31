package com.amitesh.letsConnect.api.dto

import com.amitesh.letsConnect.domain.type.ChatId
import com.amitesh.letsConnect.domain.type.ChatMessageId
import com.amitesh.letsConnect.domain.type.UserId
import java.time.Instant

data class ChatMessageDto (
    val id: ChatMessageId,
    val chatId: ChatId,
    val content: String,
    val createdAt: Instant,
    val senderId: UserId
)