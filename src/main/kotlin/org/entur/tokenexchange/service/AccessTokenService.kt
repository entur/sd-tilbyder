package org.entur.tokenexchange.service

import org.entur.tokenexchange.service.scope.BearerCredential
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class AccessTokenService(val restTemplate: RestTemplate, val tokenService: TokenService) {

    fun getAccessToken(): BearerCredential {

        val jwt = tokenService.getTokenForAccessToken()

        val url = "https://oauth2.googleapis.com/token"

        val map = StringBuilder()
        map.append("grant_type", "=", "urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer", "&")
        map.append("assertion", "=", jwt.token)

        val result = with(
            HttpHeaders().also {
                it.contentType = MediaType.APPLICATION_FORM_URLENCODED
            }
        ) {
            restTemplate.exchange(
                URI.create(url),
                HttpMethod.POST,
                HttpEntity<String>(map.toString(), this),
                Map::class.java
            ).body!!
        }
        val token = result.get("access_token") as String
        val expiry = result.get("expires_in") as Int

        return BearerCredential(token, expiry.toLong())
    }
}
