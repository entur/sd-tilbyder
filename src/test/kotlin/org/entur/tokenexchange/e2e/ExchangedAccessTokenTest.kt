package org.entur.tokenexchange.e2e

import org.junit.jupiter.api.Assertions.assertTrue
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
class ExchangedAccessTokenTest(
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
        val storageToken = with(
            HttpHeaders().also {
                it.setBearerAuth(
                    mportenToken
                )
            }
        ) {
            restTemplate.exchange(
                "http://localhost:$randomServerPort/accesstoken",
                HttpMethod.GET,
                HttpEntity<String>(this),
                String::class.java
            ).body!!
        }

        val result = with(
            HttpHeaders().also {
                it.setBearerAuth(
                    storageToken
                )
            }
        ) {
            restTemplate.exchange(
                "https://storage.googleapis.com/storage/v1/b/tverr_test_test_test/o",
                HttpMethod.GET,
                HttpEntity<String>(this),
                Map::class.java
            )
        }
        assert(result.statusCode == HttpStatus.OK)
        assertTrue((result.body!!.get("items") as ArrayList<*>).size > 1)
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
