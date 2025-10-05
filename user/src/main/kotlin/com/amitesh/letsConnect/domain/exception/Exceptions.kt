package com.amitesh.letsConnect.domain.exception

import java.lang.RuntimeException

class UserAlreadyExistException: RuntimeException(
    "A user with this email or username is already exists."
)

class UsernameEmptyException: RuntimeException(
    "Username shouldn't be empty."
)

class EmailEmptyException: RuntimeException(
    "Email shouldn't be empty."
)

class PasswordEmptyException: RuntimeException(
    "Password shouldn't be empty."
)