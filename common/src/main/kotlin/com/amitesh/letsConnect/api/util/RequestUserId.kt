package com.amitesh.letsConnect.api.util

import com.amitesh.letsConnect.domain.type.UserId
import com.amitesh.letsConnect.domain.exception.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()