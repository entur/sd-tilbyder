package org.entur.tokenexchange.service.scope

import org.entur.tokenexchange.service.TokenService
import org.springframework.stereotype.Service

@Service
class SkyssAPCBigQueryDistribution(val tokenService: TokenService) : ScopeToDistribution {

    override fun getScope(): Scope = Scope.SKYSS_APC

    override fun getDistribution(): Dataset {
        val jwt = tokenService.getTokenForAudience("https://bigquery.googleapis.com/")
        return Dataset(
            "https://bigquery.googleapis.com/bigquery/v2/projects/entur-data-external/datasets/realtime_siri_et_view/tables/realtime_siri_et_last_recorded_view",
            jwt,
            getScope()
        )
    }
}
