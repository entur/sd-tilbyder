package org.entur.tokenexchange.service.scope

interface ScopeToDistribution {
    fun getScope(): Scope
    fun getDistribution(): Dataset
}

enum class Scope(val scopeValue: String) {
    SKYSS_APC("entur:skyss.1")
}

data class Dataset(
    val url: String,
    val credential: Credential,
    val scope: Scope
)
sealed class Credential(val authType: String)

data class BearerCredential( // add format?
    val token: String,
    val expiresIn: Long
) : Credential("Bearer") // enum
