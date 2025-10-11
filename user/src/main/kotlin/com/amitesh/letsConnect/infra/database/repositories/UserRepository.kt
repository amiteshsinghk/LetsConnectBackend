package com.amitesh.letsConnect.infra.database.repositories

import com.amitesh.letsConnect.domain.model.UserId
import com.amitesh.letsConnect.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}