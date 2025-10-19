package com.amitesh.letsConnect.service.auth

import com.amitesh.letsConnect.domain.exception.InvalidCredentialException
import com.amitesh.letsConnect.domain.exception.InvalidTokenException
import com.amitesh.letsConnect.domain.exception.UserAlreadyExistException
import com.amitesh.letsConnect.domain.exception.UserNotFoundException
import com.amitesh.letsConnect.domain.model.AuthenticatedUser
import com.amitesh.letsConnect.domain.model.User
import com.amitesh.letsConnect.domain.model.UserId
import com.amitesh.letsConnect.infra.database.entities.RefreshTokenEntity
import com.amitesh.letsConnect.infra.database.entities.UserEntity
import com.amitesh.letsConnect.infra.database.mappers.toUser
import com.amitesh.letsConnect.infra.database.repositories.RefreshTokenRepository
import com.amitesh.letsConnect.infra.database.repositories.UserRepository
import com.amitesh.letsConnect.infra.security.PasswordEncoder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun register(email: String, username: String, password: String): User {
        val userExist = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim()
        )
        if (userExist != null){
            when{
                userExist.email.equals(email, ignoreCase = true) -> throw UserAlreadyExistException("email '${userExist.email}'" )
                userExist.username.equals(username, ignoreCase = true) -> throw UserAlreadyExistException("username '${userExist.username}'")
            }

        }
        val saveduser = userRepository.save(
            UserEntity(
                email = email.trim(),
                username = username.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()
        return saveduser
    }

    fun login(
        email: String,
        password: String
    ): AuthenticatedUser {
        val user = userRepository.findByEmail(email = email.trim())
            ?: throw InvalidCredentialException()

        if(!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialException()
        }

        //TODO: Check for verified email

        return  user.id?.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId = userId)
            val refreshToken = jwtService.generateRefreshToken(userId = userId)
            storeRefreshToken(userId, refreshToken)
            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional
    fun refresh( refreshToken: String): AuthenticatedUser {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException(
                message = "Invalid refresh token"
            )
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findByIdOrNull(userId) ?:
        throw UserNotFoundException()

        val hashed = hashedToken(refreshToken)

        return  user.id?.let { userId ->
            refreshTokenRepository.findByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            ) ?: throw InvalidTokenException("Invalid refresh taken")
            refreshTokenRepository.deleteByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            )

            val newAccessToken = jwtService.generateAccessToken(userId)
            val newRefrehToken = jwtService.generateRefreshToken(userId = userId)

            storeRefreshToken(userId, newRefrehToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = newAccessToken,
                refreshToken = newRefrehToken
            )

        } ?: throw UserNotFoundException()
    }

    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashed = hashedToken(token)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashedToken(token: String): String{
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashedBytes)
    }
}