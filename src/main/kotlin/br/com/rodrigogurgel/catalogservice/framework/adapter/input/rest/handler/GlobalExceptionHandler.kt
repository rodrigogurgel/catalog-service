package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.handler

import br.com.rodrigogurgel.catalogservice.application.exception.BeginsWithLengthException
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductIsInUseException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationMinPermittedException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationOptionsIsEmptyException
import br.com.rodrigogurgel.catalogservice.domain.exception.DescriptionLengthException
import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedCustomizationException
import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedOptionException
import br.com.rodrigogurgel.catalogservice.domain.exception.MalformedImagePathURLException
import br.com.rodrigogurgel.catalogservice.domain.exception.NameLengthException
import br.com.rodrigogurgel.catalogservice.domain.exception.OfferPriceZeroException
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.exception.PriceNegativeException
import br.com.rodrigogurgel.catalogservice.domain.exception.QuantityMaxPermittedException
import br.com.rodrigogurgel.catalogservice.domain.exception.QuantityMaxPermittedZeroException
import br.com.rodrigogurgel.catalogservice.domain.exception.QuantityMinPermittedException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun defaultExceptionHandler(e: Exception): ResponseEntity<ProblemDetail> {
        logger.error("An unexpected error occurred", e)

        val problemDetail =
            ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
        problemDetail.title = "Internal server error"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        logger.warn(ex.message)

        val problemDetail =
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Failed to read request")
        problemDetail.title = "Bad Request"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BeginsWithLengthException::class)
    fun exceptionHandler(e: BeginsWithLengthException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter length begins with"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CategoryAlreadyExistsException::class)
    fun exceptionHandler(e: CategoryAlreadyExistsException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message)
        problemDetail.title = "Already exists a Category with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(CategoryNotFoundException::class)
    fun exceptionHandler(e: CategoryNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "No Category founded with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(OfferAlreadyExistsException::class)
    fun exceptionHandler(e: OfferAlreadyExistsException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message)
        problemDetail.title = "Already exists an Offer with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(OfferNotFoundException::class)
    fun exceptionHandler(e: OfferNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "No Offer founded with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ProductAlreadyExistsException::class)
    fun exceptionHandler(e: ProductAlreadyExistsException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message)
        problemDetail.title = "Already exists a Product with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(ProductIsInUseException::class)
    fun exceptionHandler(e: ProductIsInUseException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "The Product with the Id specified is in use"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun exceptionHandler(e: ProductNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "No Product founded with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ProductsNotFoundException::class)
    fun exceptionHandler(e: ProductsNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "One or more Products are not founded"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(StoreNotFoundException::class)
    fun exceptionHandler(e: StoreNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "No Store founded with the specified Id"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CustomizationAlreadyExistsException::class)
    fun exceptionHandler(e: CustomizationAlreadyExistsException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message)
        problemDetail.title = "Already exists a Customization with the specified Id in the Offer"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(CustomizationMinPermittedException::class)
    fun exceptionHandler(e: CustomizationMinPermittedException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter Customization quantity min permitted"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CustomizationNotFoundException::class)
    fun exceptionHandler(e: CustomizationNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "No Customization founded with the specified Id in the Offer"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CustomizationOptionsIsEmptyException::class)
    fun exceptionHandler(e: CustomizationOptionsIsEmptyException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid state Customization Options is empty"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DescriptionLengthException::class)
    fun exceptionHandler(e: DescriptionLengthException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter length Description"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DuplicatedCustomizationException::class)
    fun exceptionHandler(e: DuplicatedCustomizationException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid state duplicated Customization Id in the Offer"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DuplicatedOptionException::class)
    fun exceptionHandler(e: DuplicatedOptionException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid state duplicated Option Id in the Offer"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MalformedImagePathURLException::class)
    fun exceptionHandler(e: MalformedImagePathURLException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter Image path is not a valid URL"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NameLengthException::class)
    fun exceptionHandler(e: NameLengthException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter length Name"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(OfferPriceZeroException::class)
    fun exceptionHandler(e: OfferPriceZeroException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter the Offer price cannot be zero"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(OptionAlreadyExistsException::class)
    fun exceptionHandler(e: OptionAlreadyExistsException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message)
        problemDetail.title = "Already exists a Option with the specified Id in the Offer"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(OptionNotFoundException::class)
    fun exceptionHandler(e: OptionNotFoundException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "No Option founded with the specified Id in the Offer"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PriceNegativeException::class)
    fun exceptionHandler(e: PriceNegativeException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter Price negative"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(QuantityMaxPermittedException::class)
    fun exceptionHandler(e: QuantityMaxPermittedException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter Quantity Max Permitted"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(QuantityMaxPermittedZeroException::class)
    fun exceptionHandler(e: QuantityMaxPermittedZeroException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter Quantity Max Permitted is Zero"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(QuantityMinPermittedException::class)
    fun exceptionHandler(e: QuantityMinPermittedException): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message)
        problemDetail.title = "Invalid parameter Quantity Min Permitted"
        problemDetail.properties = mapOf("timestamp" to Instant.now().epochSecond)

        return ResponseEntity(problemDetail, HttpStatus.BAD_REQUEST)
    }
}
