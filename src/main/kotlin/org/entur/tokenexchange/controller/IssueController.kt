package org.entur.tokenexchange.controller

import org.entur.server.api.IssueApi
import org.entur.server.viewmodel.Credential
import org.entur.server.viewmodel.DistributionCredential
import org.entur.tokenexchange.exception.AuthenticationException
import org.entur.tokenexchange.service.DistributionService
import org.entur.tokenexchange.service.scope.BearerCredential
import org.entur.tokenexchange.service.scope.Scope
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IssueController(
    val service: DistributionService
) : IssueApi {

    @GetMapping("/v1/issue-credential")
    override fun exchangeToken(): ResponseEntity<List<DistributionCredential>> {
        val scopes = withSecurityContext()
        val distributions = service.getDistributions(scopes)
        return distributions.map {
            val cred: Credential
            when (it.credential) {
                is BearerCredential -> {
                    cred = Credential(it.credential.token, it.credential.authType, it.credential.expiresIn.toInt())
                }
            }
            DistributionCredential(
                it.scope.scopeValue,
                it.url,
                cred
            )
        }
            .let {
                ResponseEntity.ok(it)
            }
    }

    @GetMapping("/v1/scope")
    fun scope(): ResponseEntity<List<String>> {
        return withSecurityContext().map {
            it.scopeValue
        }.let { ResponseEntity.ok(it) }
    }

    private fun withSecurityContext(): Set<Scope> {
        return SecurityContextHolder.getContext().authentication?.let {
            (it as JwtAuthenticationToken).authorities.map {
                val claimsAuthority = it.authority
                val scope = claimsAuthority.removePrefix("SCOPE_")
                Scope.values().first { it.scopeValue == scope }
            }.toSet()
        } ?: throw AuthenticationException("Failed to get valid scope from authentication")
    }
}
