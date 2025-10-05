package com.amitesh.letsConnect.api.dto

import com.amitesh.letsConnect.domain.model.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean
)
