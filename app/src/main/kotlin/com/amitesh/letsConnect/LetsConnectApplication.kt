package com.amitesh.letsConnect

import com.amitesh.com.amitesh.chats.Testing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LetsConnectApplication

fun main(args: Array<String>) {
    Testing()
	runApplication<LetsConnectApplication>(*args)
}
