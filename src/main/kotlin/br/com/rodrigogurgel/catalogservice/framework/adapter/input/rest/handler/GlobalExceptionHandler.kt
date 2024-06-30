package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.handler

import br.com.rodrigogurgel.catalogservice.domain.exception.MalformedImagePathURLException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(MalformedImagePathURLException::class)
    fun malformedImagePathURLException(e: MalformedImagePathURLException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Malformed Image Path URL"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)
        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }
}
