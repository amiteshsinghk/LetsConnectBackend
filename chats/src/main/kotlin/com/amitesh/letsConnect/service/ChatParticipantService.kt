package com.amitesh.letsConnect.service

import com.amitesh.letsConnect.infra.database.mappers.toChatParticipant
import com.amitesh.letsConnect.infra.database.mappers.toChatParticipantEntity
import com.amitesh.letsConnect.infra.database.repositories.ChatParticipantRepository
import com.amitesh.letsConnect.domain.models.ChatParticipant
import com.amitesh.letsConnect.domain.type.UserId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatParticipantService(
    private val chatParticipantRepository: ChatParticipantRepository
) {

    fun createChatParticipant(
        chatParticipant: ChatParticipant
    ) {
        println("ChatParticipantService :: createChatParticipant :: $chatParticipant")
        chatParticipantRepository.save(
            chatParticipant.toChatParticipantEntity()
        )
    }

    fun findChatParticipantById(userId: UserId): ChatParticipant?{
        return chatParticipantRepository.findByIdOrNull(
            userId
        )?.toChatParticipant()
    }


    fun findChatParticipantByEmailOrUsername(
        query: String
    ): ChatParticipant? {
        val normalizedQuery = query.lowercase().trim()
        return chatParticipantRepository.findByEmailOrUsername(
            query = normalizedQuery
        )?.toChatParticipant()
    }
}