package com.amitesh.letsConnect.domain.exception

import java.lang.RuntimeException

class UserAlreadyExistException(user: String?): RuntimeException(
    "A user with this $user is already exists."
)
