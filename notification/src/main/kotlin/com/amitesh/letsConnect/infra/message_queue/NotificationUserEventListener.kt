package com.amitesh.letsConnect.infra.message_queue

import com.amitesh.letsConnect.domain.events.user.UserEvent
import com.amitesh.letsConnect.service.EmailService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
class NotificationUserEventListener(private val emailService: EmailService) {

    @RabbitListener(queues = [MessageQueues.NOTIFICATION_USER_EVENTS])
    @Transactional
    fun handleUserEvent(userEvent: UserEvent){
        println("NotificationUserEventListener :: handleUserEvent! ==> $userEvent")
        when(userEvent){
            is UserEvent.Created -> {
                println("User created!")
                emailService.sendVerificationEmail(
                    email = userEvent.email,
                    username = userEvent.username,
                    userId = userEvent.userId,
                    token = userEvent.verificationToken
                )
            }
            is UserEvent.RequestResendVerification -> {
                println("Request resend verification!")
                emailService.sendVerificationEmail(
                    email = userEvent.email,
                    username = userEvent.username,
                    userId = userEvent.userId,
                    token = userEvent.verificationToken
                )
            }
            is UserEvent.RequestResetPassword -> {
                println("Request resend password!")
                emailService.sendPasswordResetEmail(
                    email = userEvent.email,
                    username = userEvent.username,
                    userId = userEvent.userId,
                    token = userEvent.passwordResetToken,
                    expiresIn = Duration.ofMinutes(userEvent.expiresInMinutes)
                )
            }
            else -> Unit
        }
    }

}