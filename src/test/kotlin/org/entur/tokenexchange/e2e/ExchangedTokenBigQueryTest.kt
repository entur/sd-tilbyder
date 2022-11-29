package org.entur.tokenexchange.e2e

import org.entur.tokenexchange.service.scope.Scope
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangedTokenBigQueryTest(
    @Value("\${mporten.username}") val mportenUsername: String,
    @Value("\${mporten.password}") val mportenPass: String,
    @Value("\${mporten.tokenendpoint}") val mportenTokenEndpoint: String
) {
    @LocalServerPort
    private var randomServerPort: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should be able to use exchanged token to run API calls against BQ`() {
        val mportenToken = getMportenToken(mportenUsername, mportenPass)
        val distributionCredentials = with(
            HttpHeaders().also {
                it.setBearerAuth(
                    mportenToken
                )
            }
        ) {
            restTemplate.exchange(
                "http://localhost:$randomServerPort/v1/issue-credential",
                HttpMethod.GET,
                HttpEntity<String>(this),
                List::class.java
            ).body!!
        }

        val dc = distributionCredentials.get(0) as LinkedHashMap<String, *>
        val scope = dc.get("scope") as String
        val url = dc.get("url") as String
        val credential = dc.get("credential") as LinkedHashMap<String, *>

        val result = with(
            HttpHeaders().also {
                it.setBearerAuth(
                    credential.get("token") as String
                )
            }
        ) {
            restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity<String>(this),
                Map::class.java
            )
        }
        assert(scope == Scope.SKYSS_APC.scopeValue)
        assert(result.statusCode == HttpStatus.OK)
        assert(result.body!!["id"]!! == "entur-data-external:realtime_siri_et_view.realtime_siri_et_last_recorded_view")
    }

    private fun getMportenToken(userName: String, password: String): String =
        with(
            HttpHeaders().also {
                it.setBasicAuth(userName, password)
            }
        ) {
            restTemplate.exchange(
                mportenTokenEndpoint,
                HttpMethod.GET,
                HttpEntity<String>(this),
                Map::class.java
            )
        }.body!!["access_token"] as String
}
