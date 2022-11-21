package org.entur.tokenexchange.service.scope

import org.entur.tokenexchange.service.AccessTokenService
import org.springframework.stereotype.Service

@Service
class SkyssAPCFileDistribution(val tokenService: AccessTokenService) : ScopeToDistribution {

    override fun getScope(): Scope = Scope.SKYSS_APC
    override fun getDistribution(): Dataset {
        val jwt = tokenService.getAccessToken()
        return Dataset(
            "https://storage.googleapis.com/storage/v1/b/tverr_test_test_test/o",
            jwt,
            getScope()
        )
    }
}
