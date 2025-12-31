package com.amitesh.letsConnect.domain.exception

import com.amitesh.letsConnect.domain.type.UserId

class ChatParticipantNotFoundException(
    private val id: UserId
): RuntimeException(
    "The chat participant with the ID $id was not found."
)