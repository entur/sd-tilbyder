package org.entur.tokenexchange.service

import org.entur.tokenexchange.service.scope.Dataset
import org.entur.tokenexchange.service.scope.Scope
import org.entur.tokenexchange.service.scope.ScopeToDistribution
import org.springframework.stereotype.Service

@Service
class DistributionService(val toDatasets: List<ScopeToDistribution>) {

    fun knownScopes(): Set<Scope> = toDatasets.map { it.getScope() }.toSet()

    fun getDistributions(scopes: Set<Scope>): Set<Dataset> {
        return toDatasets
            .filter {
                scopes.contains(it.getScope())
            }.map {
                it.getDistribution()
            }.toSet()
    }
}
