package org.entur.tokenexchange.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandling {

    // ErrorResponseObject?

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    fun handleAuthException(e: AuthenticationException, request: HttpServletRequest): ResponseEntity<String> =
        ResponseEntity(
            e.reason,
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            },
            HttpStatus.UNAUTHORIZED
        )
}
