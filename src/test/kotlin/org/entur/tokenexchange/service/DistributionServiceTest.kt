package org.entur.tokenexchange.service

import org.entur.tokenexchange.service.scope.BearerCredential
import org.entur.tokenexchange.service.scope.Dataset
import org.entur.tokenexchange.service.scope.Scope
import org.entur.tokenexchange.service.scope.ScopeToDistribution
import org.entur.tokenexchange.service.scope.SkyssAPCBigQueryDistribution
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DistributionServiceTest {

    val mock = mock(TokenService::class.java)

    @BeforeEach
    internal fun setUp() {
        `when`(mock.getToken()).thenReturn(BearerCredential("mock_token", 0L))
    }

    @Test
    fun knownScopes() {
        val distributionService = DistributionService(listOf(SkyssAPCBigQueryDistribution(mock)))
        val scopes = distributionService.knownScopes()
        assertEquals(scopes, setOf(Scope.SKYSS_APC))
    }

    @Test
    fun knownMultipleSetsForScope() {
        val distributionService = DistributionService(listOf(SkyssAPCBigQueryDistribution(mock), FileDistribution()))
        val scopes = distributionService.knownScopes()
        assertEquals(scopes, setOf(Scope.SKYSS_APC))
    }

    @Test
    fun singleFakeDistribution() {
        val scope = Scope.SKYSS_APC
        val distributionService = DistributionService(listOf(FileDistribution()))
        val datasets = distributionService.getDistributions(setOf(scope))
        val dataset = datasets.single()
        assertEquals(dataset.scope, scope)
        val credential = dataset.credential as BearerCredential
        assertEquals(credential.authType, "Bearer")
        assertEquals(credential.token, FileDistribution.token)
        assertEquals(dataset.url, FileDistribution.url)
    }

    class FileDistribution : ScopeToDistribution {

        companion object {
            val url = "fillink"
            val token = "faketoken"
        }

        override fun getScope(): Scope = Scope.SKYSS_APC

        override fun getDistribution(): Dataset = Dataset(url, BearerCredential(token, 0L), getScope())
    }
}
