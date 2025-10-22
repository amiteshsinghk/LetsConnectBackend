package com.amitesh.letsConnect.infra.database.repositories

import com.amitesh.letsConnect.infra.database.entities.PasswordResetTokenEntity
import com.amitesh.letsConnect.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant


interface PasswordResetTokenRepository: JpaRepository<PasswordResetTokenEntity, Long> {
    fun findByToken(token: String): PasswordResetTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)
//    fun findByUserAndUsedAtIsNull(user: UserEntity): List<PasswordResetTokenEntity>

    @Modifying
    @Query("""
        UPDATE PasswordResetTokenEntity p
        SET p.usedAt = CURRENT_TIMESTAMP
        WHERE p.user = :user
    """)
    fun invalidateActiveTokenForUser(user: UserEntity)
}