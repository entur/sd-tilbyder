package org.entur.tokenexchange.controller

import org.entur.tokenexchange.exception.AuthenticationException
import org.entur.tokenexchange.service.scope.Scope
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class SecurityContext {

    fun withSecurityContext(): Set<Scope> {
        return SecurityContextHolder.getContext().authentication?.let {
            (it as JwtAuthenticationToken).authorities.map {
                val claimsAuthority = it.authority
                val scope = claimsAuthority.removePrefix("SCOPE_")
                Scope.values().first { it.scopeValue == scope }
            }.toSet()
        } ?: throw AuthenticationException("Failed to get valid scope from authentication")
    }

    fun withClientContext(): String {
        SecurityContextHolder.getContext().authentication?.let {
            val jwtAuthenticationToken = it as JwtAuthenticationToken
            return jwtAuthenticationToken.token.claims["client_id"] as String
        } ?: throw AuthenticationException("Failed to get valid scope from authentication")
    }

    fun withConsumerContext(): Map<String, String> {
        SecurityContextHolder.getContext().authentication?.let {
            val jwtAuthenticationToken = it as JwtAuthenticationToken
            val c = jwtAuthenticationToken.token.claims["consumer"] as Map<String, String>
            return c
        } ?: throw AuthenticationException("Failed to get valid scope from authentication")
    }
}
