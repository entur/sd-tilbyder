package org.entur.tokenexchange.service.scope.skyssapc

import org.entur.tokenexchange.service.AccessTokenService
import org.entur.tokenexchange.service.scope.Dataset
import org.entur.tokenexchange.service.scope.Scope
import org.entur.tokenexchange.service.scope.ScopeToDistribution
import org.springframework.stereotype.Service

@Service
class SkyssAPCFileDistribution(val tokenService: AccessTokenService, val serviceAccount: SkyssAPCServiceAccount) : ScopeToDistribution {

    override fun getScope(): Scope = Scope.SKYSS_APC

    override fun getDistribution(): Dataset {
        val jwt = tokenService.getAccessToken(serviceAccount.getCredentials())
        return Dataset(
            "https://storage.googleapis.com/storage/v1/b/skyss_apc_sharing-dev/o",
            jwt,
            getScope()
        )
    }
}
