package com.amitesh.letsConnect.infra.messaging

import com.amitesh.letsConnect.service.ChatParticipantService
import com.amitesh.letsConnect.domain.events.user.UserEvent
import com.amitesh.letsConnect.domain.models.ChatParticipant
import com.amitesh.letsConnect.infra.message_queue.MessageQueues
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatUserEventListener(
    private val chatParticipantService: ChatParticipantService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

@RabbitListener(queues = [MessageQueues.CHAT_USER_EVENTS])
@Transactional
    fun handleUserEvent(event: UserEvent){
    logger.info("ChatUserEventListener :: handleUserEvent! ==> $event")
        when(event){
            is UserEvent.Verified -> {
                chatParticipantService.createChatParticipant(
                    chatParticipant = ChatParticipant(
                        userId = event.userId,
                        username = event.username,
                        email = event.email,
                        profilePictureUrl = null
                    )
                )
            }
            else -> Unit
        }
    }
}