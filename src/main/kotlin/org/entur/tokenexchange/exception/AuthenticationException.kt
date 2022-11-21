package org.entur.tokenexchange.exception

data class AuthenticationException(
    val reason: String
) : RuntimeException()
