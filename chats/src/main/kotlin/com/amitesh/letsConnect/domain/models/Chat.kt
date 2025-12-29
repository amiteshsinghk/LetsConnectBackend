package com.amitesh.letsConnect.domain.models

import com.amitesh.letsConnect.domain.type.ChatId
import java.time.Instant

data class Chat(
    val id: ChatId,
    val participant: Set<ChatParticipant>,
    val lastMessage: ChatMessage?,
    val creator: ChatParticipant,
    val lastActivityAt: Instant,
    val createdAt: Instant
)
