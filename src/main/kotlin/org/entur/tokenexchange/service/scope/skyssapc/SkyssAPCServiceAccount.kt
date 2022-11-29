package org.entur.tokenexchange.service.scope.skyssapc

import com.google.auth.oauth2.ServiceAccountCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SkyssAPCServiceAccount(@Value("\${saKey}") val serviceAccountKey: String) {
    private val saCredential: ServiceAccountCredentials =
        ServiceAccountCredentials.fromStream(serviceAccountKey.byteInputStream())

    fun getCredentials() = saCredential
}
