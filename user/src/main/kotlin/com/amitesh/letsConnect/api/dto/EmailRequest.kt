package com.amitesh.letsConnect.api.dto

import jakarta.validation.constraints.Email

data class EmailRequest(
    @field: Email
    val email: String,
)
