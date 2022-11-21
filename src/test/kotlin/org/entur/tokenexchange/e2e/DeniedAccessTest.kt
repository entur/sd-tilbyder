package org.entur.tokenexchange.e2e

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeniedAccessTest() {
    @LocalServerPort
    private var randomServerPort: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @ParameterizedTest
    @ValueSource(strings = ["v1/scope", "v1/issue-credential", "token", "accesstoken"])
    internal fun deniedAnonymousGETForPath(path: String) {
        val response =
            restTemplate.exchange(
                "http://localhost:$randomServerPort/$path",
                HttpMethod.GET,
                HttpEntity<String>(HttpHeaders()),
                List::class.java
            )

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }
}
