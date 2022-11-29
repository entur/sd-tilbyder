package org.entur.tokenexchange.controller

import org.entur.server.api.IssueApi
import org.entur.server.viewmodel.Credential
import org.entur.server.viewmodel.DistributionCredential

import org.entur.tokenexchange.service.DistributionService
import org.entur.tokenexchange.service.scope.BearerCredential

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IssueController(
    val service: DistributionService,
    val securityContext: SecurityContext
) : IssueApi {

    @GetMapping("/v1/issue-credential")
    override fun exchangeToken(): ResponseEntity<List<DistributionCredential>> {
        val scopes = securityContext.withSecurityContext()
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


}
