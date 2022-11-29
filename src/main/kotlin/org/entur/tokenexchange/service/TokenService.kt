package org.entur.tokenexchange.service

import org.entur.tokenexchange.repository.TokenRepository
import org.entur.tokenexchange.service.scope.BearerCredential
import org.entur.tokenexchange.service.scope.Credential
import org.springframework.stereotype.Service

@Service
class TokenService(val repo: TokenRepository) {

    fun getTokenForAudience(audience: String): Credential = repo.getJwt(audience)

    fun getTokenForAccessToken(): BearerCredential = repo.getJwt("https://oauth2.googleapis.com/token", "https://www.googleapis.com/auth/cloud-platform")
}
