package com.amitesh.letsConnect.infra.database.mappers

import com.amitesh.letsConnect.infra.database.entities.ChatEntity
import com.amitesh.letsConnect.domain.models.Chat
import com.amitesh.letsConnect.domain.models.ChatMessage
import com.amitesh.letsConnect.domain.models.ChatParticipant
import com.amitesh.letsConnect.infra.database.entities.ChatParticipantEntity

fun ChatEntity.toChat(lastMessage: ChatMessage? = null): Chat{
    return Chat(
        id = id!!,
        participants = participants.map {
            it.toChatParticipant()
        }.toSet(),
        creator = creator.toChatParticipant(),
        lastActivityAt = lastMessage?.createdAt ?: createdAt,
        createdAt = createdAt,
        lastMessage = lastMessage
    )
}

fun ChatParticipantEntity.toChatParticipant(): ChatParticipant{
    return ChatParticipant(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipant.toChatParticipantEntity(): ChatParticipantEntity{
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}