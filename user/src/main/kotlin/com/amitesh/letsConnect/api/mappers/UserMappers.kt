package com.amitesh.letsConnect.api.mappers

import com.amitesh.letsConnect.api.dto.AuthenticatedUserDto
import com.amitesh.letsConnect.api.dto.UserDto
import com.amitesh.letsConnect.domain.model.AuthenticatedUser
import com.amitesh.letsConnect.domain.model.User

fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto {
    return AuthenticatedUserDto(
        user = user.toUserDto(),
        accessToken = accessToken,
        refreshToken = refreshToken
    )

}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerifid
    )
}