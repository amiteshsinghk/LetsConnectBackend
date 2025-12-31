package com.amitesh.letsConnect.service

import com.amitesh.letsConnect.domain.exception.ChatParticipantNotFoundException
import com.amitesh.letsConnect.domain.exception.InvalidChatSizeException
import com.amitesh.letsConnect.infra.database.entities.ChatEntity
import com.amitesh.letsConnect.infra.database.mappers.toChat
import com.amitesh.letsConnect.infra.database.repositories.ChatParticipantRepository
import com.amitesh.letsConnect.infra.database.repositories.ChatRepository
import com.amitesh.letsConnect.domain.models.Chat
import com.amitesh.letsConnect.domain.type.UserId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
) {
    @Transactional
    fun createChat(
        creatorId: UserId,
        otherUserIds: Set<UserId>
    ): Chat{
        val otherParticipants = chatParticipantRepository.findByUserIdIn(
            userIds = otherUserIds
        )

        val allParticipant = (otherParticipants + creatorId)
        if (allParticipant.size < 2){
            throw InvalidChatSizeException()
        }

        val creator = chatParticipantRepository.findByIdOrNull(creatorId)
            ?: throw ChatParticipantNotFoundException(creatorId)
        return chatRepository.save(
            ChatEntity(
                creator = creator,
                participants = setOf(creator) + otherParticipants,
            )
        ).toChat(lastMessage = null)
    }
}