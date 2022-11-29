package org.entur.tokenexchange.e2e

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScopeMappingTest (
    @Value("\${mporten.username}") val mportenUsername: String,
    @Value("\${mporten.password}") val mportenPass: String,
    @Value("\${mporten.tokenendpoint}") val mportenTokenEndpoint: String
) {
    @LocalServerPort
    private var randomServerPort: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate


    @Test
    internal fun tokenmapping() {
        val mportenToken = getMportenToken(mportenUsername, mportenPass)
        val auth = with(
            HttpHeaders().also {
                it.setBearerAuth(
                    mportenToken
                )
            }
        ) {
            restTemplate.exchange(
                "http://localhost:$randomServerPort/scope",
                HttpMethod.GET,
                HttpEntity<String>(this),
                List::class.java
            ).body!!
        }

        Assertions.assertEquals("entur:skyss.1", auth.get(0))
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