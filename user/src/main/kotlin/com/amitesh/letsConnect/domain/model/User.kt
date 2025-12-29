package com.amitesh.letsConnect.domain.model

import com.amitesh.letsConnect.domain.type.UserId

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerifid: Boolean
)
