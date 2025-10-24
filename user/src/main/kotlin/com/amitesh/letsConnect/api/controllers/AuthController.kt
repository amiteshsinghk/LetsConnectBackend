package com.amitesh.letsConnect.api.controllers

import com.amitesh.letsConnect.api.dto.AuthenticatedUserDto
import com.amitesh.letsConnect.api.dto.ChangePasswordRequest
import com.amitesh.letsConnect.api.dto.EmailRequest
import com.amitesh.letsConnect.api.dto.LoginRequest
import com.amitesh.letsConnect.api.dto.RefreshRequest
import com.amitesh.letsConnect.api.dto.RegisterRequest
import com.amitesh.letsConnect.api.dto.ResetPasswordRequest
import com.amitesh.letsConnect.api.dto.UserDto
import com.amitesh.letsConnect.api.mappers.toAuthenticatedUserDto
import com.amitesh.letsConnect.api.mappers.toUserDto
import com.amitesh.letsConnect.infra.rate_limiting.EmailRateLimiter
import com.amitesh.letsConnect.service.AuthService
import com.amitesh.letsConnect.service.EmailVerificationService
import com.amitesh.letsConnect.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService,
    private val emailRateLimiter: EmailRateLimiter
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

    @PostMapping("/resend-verification")
    fun resendVerification(
        @Valid @RequestBody body: EmailRequest
    ){
        emailRateLimiter.withRateLimit(
            email = body.email
        ){
            emailVerificationService.resendVerificationEmail(body.email)
        }

    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ){
        passwordResetService.requestPasswordReset(body.email)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @Valid @RequestBody body: ResetPasswordRequest
    ){
        passwordResetService.resetPassword(
            token = body.token,
            newPassword = body.newPassword
        )
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ){
        // TODO: Extract request userId and call service
    }
}