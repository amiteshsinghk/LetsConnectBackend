package com.amitesh.letsConnect.infra.security

import com.amitesh.letsConnect.domain.exception.EmailEmptyException
import com.amitesh.letsConnect.domain.exception.PasswordEmptyException
import com.amitesh.letsConnect.domain.exception.UserAlreadyExistException
import com.amitesh.letsConnect.domain.exception.UsernameEmptyException
import com.amitesh.letsConnect.domain.model.User
import com.amitesh.letsConnect.infra.database.entities.UserEntity
import com.amitesh.letsConnect.infra.database.mappers.toUser
import com.amitesh.letsConnect.infra.database.repositories.UserRepository

class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(email: String, username: String, password: String): User{
        require(email.isNotEmpty()) { throw EmailEmptyException() }
        require(username.isNotEmpty()) { throw UsernameEmptyException() }
        require(password.isNotEmpty()) { throw PasswordEmptyException() }

        val userExist = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim()
        )
        if (userExist != null){
            throw UserAlreadyExistException()
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