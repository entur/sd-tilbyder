package org.entur.tokenexchange

import org.apache.commons.logging.LogFactory
import org.entur.server.api.IssueApi
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TokenExchangeApplication

fun main(args: Array<String>) {
    runApplication<TokenExchangeApplication>(*args)
}
