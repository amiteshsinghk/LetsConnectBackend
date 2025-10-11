package com.amitesh.letsConnect.service.auth

import com.amitesh.letsConnect.domain.exception.UserAlreadyExistException
import com.amitesh.letsConnect.domain.model.User
import com.amitesh.letsConnect.infra.database.entities.UserEntity
import com.amitesh.letsConnect.infra.database.mappers.toUser
import com.amitesh.letsConnect.infra.database.repositories.UserRepository
import com.amitesh.letsConnect.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
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
}