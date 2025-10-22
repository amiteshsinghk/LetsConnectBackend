package com.amitesh.letsConnect.service

import com.amitesh.letsConnect.domain.exception.InvalidCredentialException
import com.amitesh.letsConnect.domain.exception.InvalidTokenException
import com.amitesh.letsConnect.domain.exception.SamePasswordException
import com.amitesh.letsConnect.domain.exception.UserNotFoundException
import com.amitesh.letsConnect.domain.model.UserId
import com.amitesh.letsConnect.infra.database.entities.PasswordResetTokenEntity
import com.amitesh.letsConnect.infra.database.repositories.PasswordResetTokenRepository
import com.amitesh.letsConnect.infra.database.repositories.RefreshTokenRepository
import com.amitesh.letsConnect.infra.database.repositories.UserRepository
import com.amitesh.letsConnect.infra.security.PasswordEncoder
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    @param:Value("\${letsconnect.email.reset-password.expiry-minutes}") private val expiryMinutes: Long,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    @Transactional
    fun requestPasswordReset(email: String){
        val user = userRepository.findByEmail(email) ?: return

        passwordResetTokenRepository.invalidateActiveTokenForUser(user)

        val token = PasswordResetTokenEntity(
            user = user,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES),
        )
        passwordResetTokenRepository.save(token)

        // TODO: Inform notification service about password reset trigger to send email
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String){
        val resetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if(resetToken.isUsed){
            throw InvalidTokenException("Email verification token is already used.")
        }

        if (resetToken.isExpired){
            throw InvalidTokenException("Email verification token has already expired.")
        }

        val user = resetToken.user
        if (passwordEncoder.matches(newPassword, user.hashedPassword)){
            throw SamePasswordException()
        }
        val hashedNewPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                this.hashedPassword = hashedNewPassword
            }
        )
        passwordResetTokenRepository.save(
            resetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        user.id?.let {
            refreshTokenRepository.deleteByUserId(
                it
            )
        }

    }

    @Transactional
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String,
    ){
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if (passwordEncoder.matches(oldPassword, user.hashedPassword)){
            throw InvalidCredentialException()
        }

        if (oldPassword == newPassword){
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(user.id!!)
        val newHashedPassword = passwordEncoder.encode(newPassword)
        userRepository.save(user.apply {
            this.hashedPassword = newHashedPassword
        })
    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens(){
        passwordResetTokenRepository.deleteByExpiresAtLessThan(now = Instant.now())
    }
}