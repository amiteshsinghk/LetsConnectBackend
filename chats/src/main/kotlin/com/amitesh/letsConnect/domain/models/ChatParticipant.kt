package com.amitesh.letsConnect.domain.models

import com.amitesh.letsConnect.domain.type.UserId

data class ChatParticipant (
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePictureUrl: String?
)