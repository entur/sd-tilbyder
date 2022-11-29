package org.entur.tokenexchange.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.ServiceAccountCredentials
import org.entur.tokenexchange.service.scope.BearerCredential
import org.springframework.stereotype.Repository
import java.security.interfaces.RSAPrivateKey
import java.time.Instant

@Repository
class TokenRepository {

    fun getJwt(saCredential: ServiceAccountCredentials, audience: String): BearerCredential {
        val expiresIn: Long = 3600
        val sign = JWT.create()
            .withKeyId(saCredential.privateKeyId)
            .withIssuer(saCredential.clientEmail)
            .withSubject(saCredential.clientEmail)
            .withAudience(audience)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(expiresIn))
            .sign(Algorithm.RSA256(null, saCredential.privateKey as RSAPrivateKey))
        return BearerCredential(sign, expiresIn)
    }

    fun getJwt(saCredential: ServiceAccountCredentials, audience: String, scope: String): BearerCredential {
        val expiresIn: Long = 3600
        val sign = JWT.create()
            .withKeyId(saCredential.privateKeyId)
            .withIssuer(saCredential.clientEmail)
            .withSubject(saCredential.clientEmail)
            .withAudience(audience)
            .withClaim("scope", scope)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(expiresIn))
            .sign(Algorithm.RSA256(null, saCredential.privateKey as RSAPrivateKey))
        return BearerCredential(sign, expiresIn)
    }
}
