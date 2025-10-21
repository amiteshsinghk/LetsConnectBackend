package com.amitesh.letsConnect.infra.database.mappers

import com.amitesh.letsConnect.domain.model.EmailVerificationToken
import com.amitesh.letsConnect.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}