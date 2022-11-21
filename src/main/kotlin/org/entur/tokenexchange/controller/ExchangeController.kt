package org.entur.tokenexchange.controller

import org.entur.tokenexchange.service.AccessTokenService
import org.entur.tokenexchange.service.TokenService
import org.entur.tokenexchange.service.scope.BearerCredential
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeController(val service: TokenService, val accessTokenService: AccessTokenService) {

    @GetMapping("/token")
    fun createGoogleToken(): String {
        val credential = service.getToken()
        credential as BearerCredential
        return credential.token
    }

    @GetMapping("/accesstoken")
    fun createStorageToken(): String {
        val credential = accessTokenService.getAccessToken()
        return credential.token
    }
}
