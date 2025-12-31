package com.amitesh.letsConnect.api.controllers

import com.amitesh.letsConnect.api.dto.ChatDto
import com.amitesh.letsConnect.api.dto.CreateChatRequest
import com.amitesh.letsConnect.api.mappers.toChatDto
import com.amitesh.letsConnect.service.ChatService
import com.amitesh.letsConnect.api.util.requestUserId
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping
    fun createChat(
        @Valid @RequestBody body: CreateChatRequest
    ): ChatDto{
        return chatService.createChat(
            creatorId = requestUserId,
            otherUserIds = body.otherUserIds.toSet()
        ).toChatDto()
    }
}