package com.amitesh.letsConnect.infra.database.mappers

import com.amitesh.letsConnect.domain.model.User
import com.amitesh.letsConnect.infra.database.entities.UserEntity

fun UserEntity.toUser(): User{
    return User(
        id = id!!,
        email = email,
        username = username,
        hasEmailVerifid = hasVerifiedemail
    )
}