package com.amitesh.letsConnect.api.controllers

import com.amitesh.letsConnect.api.dto.AuthenticatedUserDto
import com.amitesh.letsConnect.api.dto.LoginRequest
import com.amitesh.letsConnect.api.dto.RefreshRequest
import com.amitesh.letsConnect.api.dto.RegisterRequest
import com.amitesh.letsConnect.api.dto.UserDto
import com.amitesh.letsConnect.api.mappers.toAuthenticatedUserDto
import com.amitesh.letsConnect.api.mappers.toUserDto
import com.amitesh.letsConnect.service.auth.AuthService
import com.amitesh.letsConnect.service.auth.EmailVerificationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService
    ) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto{
      return authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        ).toUserDto()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto{
        return  authService.login(
            email = body.email,
            password = body.password
        ).toAuthenticatedUserDto()
    }

    @PostMapping("/refresh")
    fun refresh(
            @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService
            .refresh(body.refreshToken)
            .toAuthenticatedUserDto()
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody body: RefreshRequest
    ){
        authService.logout(body.refreshToken)
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }
}