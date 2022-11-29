package org.entur.tokenexchange.controller

import org.entur.server.api.IssueApi
import org.entur.server.viewmodel.Credential
import org.entur.server.viewmodel.DistributionCredential
import org.entur.tokenexchange.TokenExchangeApplication
import org.entur.tokenexchange.service.DistributionService
import org.entur.tokenexchange.service.scope.BearerCredential
import org.entur.tokenexchange.service.scope.Scope
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@RestController
class IssueController(
    val service: DistributionService,
    val securityContext: SecurityContext
) : IssueApi {

    val log = LoggerFactory.getLogger(TokenExchangeApplication::class.java)

    @GetMapping("/v1/issue-credential")
    override fun exchangeToken(): ResponseEntity<List<DistributionCredential>> {
        val scopes = securityContext.withSecurityContext()
        log(scopes)
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

    private fun log(scopes: Set<Scope>) {
        val callerOrganization = securityContext.withConsumerContext()
        val clientId = securityContext.withClientContext()
        val requestUrl = withRemoteAddress()
        val scopeValues = scopes.map { it.scopeValue }
        log.info("Scopes $scopeValues requested by consumer (ID=${callerOrganization.get("ID")}, authority=${callerOrganization.get("authority")}, client_id=$clientId) from remote address $requestUrl")
    }

    private fun withRemoteAddress(): String {
        return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request.remoteAddr
    }
}
