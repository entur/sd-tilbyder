package org.entur.tokenexchange.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ScopeController(var securityContext: SecurityContext) {

    @GetMapping("/scope")
    fun scope(): ResponseEntity<List<String>> {
        return securityContext.withSecurityContext().map {
            it.scopeValue
        }.let { ResponseEntity.ok(it) }
    }
}