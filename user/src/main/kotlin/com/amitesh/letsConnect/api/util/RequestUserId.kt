package com.amitesh.letsConnect.api.util

import com.amitesh.letsConnect.domain.exception.UnauthorizedException
import com.amitesh.letsConnect.domain.model.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()