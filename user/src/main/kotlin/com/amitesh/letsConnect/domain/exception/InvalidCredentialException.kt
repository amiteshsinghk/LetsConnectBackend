package com.amitesh.letsConnect.domain.exception

class InvalidCredentialException: RuntimeException(
    "The entered credential aren't valid."
) {
}