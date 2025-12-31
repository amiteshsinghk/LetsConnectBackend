package com.amitesh.letsConnect.api.dto

import com.amitesh.letsConnect.domain.type.ChatId
import java.time.Instant

data class ChatDto (
    val id: ChatId,
    val participants: List<ChatParticipantDto>,
    val lastActivityAt: Instant,
    val lastMessage: ChatMessageDto?,
    val creator: ChatParticipantDto
)