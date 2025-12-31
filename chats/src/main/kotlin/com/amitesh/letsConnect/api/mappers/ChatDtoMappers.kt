package com.amitesh.letsConnect.api.mappers

import com.amitesh.letsConnect.api.dto.ChatDto
import com.amitesh.letsConnect.api.dto.ChatMessageDto
import com.amitesh.letsConnect.api.dto.ChatParticipantDto
import com.amitesh.letsConnect.domain.models.Chat
import com.amitesh.letsConnect.domain.models.ChatMessage
import com.amitesh.letsConnect.domain.models.ChatParticipant

fun ChatParticipant.toChatParticipantDto(): ChatParticipantDto{
    return ChatParticipantDto(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatMessage.toChatMessageDto(): ChatMessageDto{
    return ChatMessageDto(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = createdAt,
        senderId = sender.userId
    )
}

fun Chat.toChatDto(): ChatDto{
    return ChatDto(
        id = id,
        participants = participants.map {
            it.toChatParticipantDto()
        },
        lastActivityAt = lastActivityAt,
        lastMessage = lastMessage?.toChatMessageDto(),
        creator = creator.toChatParticipantDto()
    )
}