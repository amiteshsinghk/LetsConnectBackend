package com.amitesh.letsConnect.domain.exception

class InvalidTokenException(
    override val message: String?
): RuntimeException(
    message ?: "INVALID_TOKEN"
) {
}