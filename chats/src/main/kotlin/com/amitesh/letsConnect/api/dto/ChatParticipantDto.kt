package com.amitesh.letsConnect.api.dto

import com.amitesh.letsConnect.domain.type.UserId

data class ChatParticipantDto(
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePictureUrl: String?
)
