package org.entur.tokenexchange.service.scope

import org.entur.tokenexchange.service.TokenService
import org.springframework.stereotype.Service

@Service
class SkyssAPCBigQueryDistribution(val tokenService: TokenService) : ScopeToDistribution {

    override fun getScope(): Scope = Scope.SKYSS_APC

    override fun getDistribution(): Dataset {
        val jwt = tokenService.getTokenForAudience("https://bigquery.googleapis.com/")
        return Dataset(
            "https://bigquery.googleapis.com/bigquery/v2/projects/ent-data-sdsharing-ext-prd/datasets/skyss_apc_sharing/tables/rpt_ext_skyss__apc",
            jwt,
            getScope()
        )
    }
}
