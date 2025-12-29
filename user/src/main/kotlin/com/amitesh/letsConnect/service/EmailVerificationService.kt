package com.amitesh.letsConnect.service

import com.amitesh.letsConnect.domain.events.user.UserEvent
import com.amitesh.letsConnect.infra.message_queue.EventPublisher
import com.amitesh.letsConnect.domain.exception.InvalidTokenException
import com.amitesh.letsConnect.domain.exception.UserNotFoundException
import com.amitesh.letsConnect.domain.model.EmailVerificationToken
import com.amitesh.letsConnect.infra.database.entities.EmailVerificationTokenEntity
import com.amitesh.letsConnect.infra.database.mappers.toEmailVerificationToken
import com.amitesh.letsConnect.infra.database.repositories.EmailVerificationTokenRepository
import com.amitesh.letsConnect.infra.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class EmailVerificationService(
    private val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    private val userRepository: UserRepository,
    @param:Value("\${letsconnect.email.verification.expiry-hour}") private val expiryHours: Long,
    private val eventPublisher: EventPublisher
) {
    @Transactional
    fun resendVerificationEmail(email: String){
        val token = createVerificationToken(email)
        if (token.user.hasEmailVerifid) {
            return
        }

        eventPublisher.publish(
            event = UserEvent.RequestResendVerification(
               userId = token.user.id,
                email = token.user.email,
                username = token.user.username,
                verificationToken = token.token
            )
        )
    }

    @Transactional
    fun createVerificationToken(email: String): EmailVerificationToken{
        val userEntity = userRepository.findByEmail(email)
            ?: throw UserNotFoundException()

        emailVerificationTokenRepository.invalidateActiveTokenForUser(userEntity)

        val token = EmailVerificationTokenEntity(
            expiresAt = Instant.now().plus(expiryHours, ChronoUnit.HOURS),
            user = userEntity
        )

        return emailVerificationTokenRepository.save(token).toEmailVerificationToken()
    }

    @Transactional
    fun verifyEmail(token: String){
        val verificationToken = emailVerificationTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Email verification token is invalid.")

        if(verificationToken.isUsed){
            throw InvalidTokenException("Email verification token is already used.")
        }

        if (verificationToken.isExpired){
            throw InvalidTokenException("Email verification token has already expired.")
        }

        emailVerificationTokenRepository.save(
            verificationToken.apply {
                this.usedAt = Instant.now()
            }
        )

        userRepository.save(
            verificationToken.user.apply {
                this.hasVerifiedemail = true
            }
        )
    }
    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        emailVerificationTokenRepository.deleteByExpiresAtLessThan(
            now = Instant.now()
        )
    }
}