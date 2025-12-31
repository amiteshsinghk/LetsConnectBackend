package com.amitesh.letsConnect.domain.exception

import java.lang.RuntimeException

class UnauthorizedException: RuntimeException("Missing auth details.")