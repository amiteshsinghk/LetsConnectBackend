package com.amitesh.letsConnect.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email

data class LoginRequest(
    val email: String,
    val password: String
)