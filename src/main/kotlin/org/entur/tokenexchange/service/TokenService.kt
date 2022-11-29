package org.entur.tokenexchange.service

import com.google.auth.oauth2.ServiceAccountCredentials
import org.entur.tokenexchange.repository.TokenRepository
import org.entur.tokenexchange.service.scope.BearerCredential
import org.entur.tokenexchange.service.scope.Credential
import org.springframework.stereotype.Service

@Service
class TokenService(val repo: TokenRepository) {

    fun getTokenForAudience(saCredential: ServiceAccountCredentials, audience: String): Credential = repo.getJwt(saCredential, audience)

    fun getTokenForAccessToken(saCredential: ServiceAccountCredentials): BearerCredential = repo.getJwt(saCredential, "https://oauth2.googleapis.com/token", "https://www.googleapis.com/auth/cloud-platform")
}
