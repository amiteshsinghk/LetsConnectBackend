package com.amitesh.letsConnect.infra.database.repositories

import com.amitesh.letsConnect.domain.model.UserId
import com.amitesh.letsConnect.infra.database.entities.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshTokenEntity, Long>{
    fun findByUserIdAndHashedToken(
        userId: UserId,
        hashedToken: String
    ): RefreshTokenEntity?

    fun deleteByUserIdAndHashedToken(userId: UserId, hashedToken: String)
    fun deleteByUserId(userId: UserId)

}