package com.amitesh.letsConnect.api.dto

import com.amitesh.letsConnect.api.util.Password
import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:NotBlank
    val oldPassword: String,
    @field:Password
    val newPassword: String,
)
