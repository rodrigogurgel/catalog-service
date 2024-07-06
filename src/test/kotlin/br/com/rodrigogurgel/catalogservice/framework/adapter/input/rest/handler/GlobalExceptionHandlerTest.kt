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
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun defaultExceptionHandler() {
        val result = handler.defaultExceptionHandler(RuntimeException("test"))

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.INTERNAL_SERVER_ERROR.value()
        problemDetail.detail shouldBe "An unexpected error occurred"
        problemDetail.title shouldBe "Internal server error"
    }

    @Test
    fun handleHttpMessageNotReadable() {
        val ex = HttpMessageNotReadableException("test", RuntimeException("test"), mockk())
        val result = handler.handleHttpMessageNotReadable(ex, HttpHeaders(), HttpStatus.BAD_REQUEST, mockk())

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.detail shouldBe "Failed to read request"
        problemDetail.title shouldBe "Bad Request"
    }

    @Test
    fun handleBeginsWithLength() {
        val ex = BeginsWithLengthException(2)
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter length begins with"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleCategoryAlreadyExists() {
        val ex = CategoryAlreadyExistsException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.CONFLICT

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.CONFLICT.value()
        problemDetail.title shouldBe "Already exists a Category with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleCategoryNotFound() {
        val ex = CategoryNotFoundException(Id(), Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "No Category founded with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleOfferAlreadyExists() {
        val ex = OfferAlreadyExistsException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.CONFLICT

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.CONFLICT.value()
        problemDetail.title shouldBe "Already exists an Offer with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleOfferNotFound() {
        val ex = OfferNotFoundException(Id(), Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "No Offer founded with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleProductAlreadyExists() {
        val ex = ProductAlreadyExistsException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.CONFLICT

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.CONFLICT.value()
        problemDetail.title shouldBe "Already exists a Product with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleProductIsInUse() {
        val ex = ProductIsInUseException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "The Product with the Id specified is in use"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleProductNotFound() {
        val ex = ProductNotFoundException(Id(), Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "No Product founded with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleProductsNotFound() {
        val ex = ProductsNotFoundException(listOf(Id()))
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "One or more Products are not founded"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleStoreNotFound() {
        val ex = StoreNotFoundException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "No Store founded with the specified Id"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleCustomizationAlreadyExists() {
        val ex = CustomizationAlreadyExistsException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.CONFLICT

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.CONFLICT.value()
        problemDetail.title shouldBe "Already exists a Customization with the specified Id in the Offer"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleCustomizationMinPermitted() {
        val ex = CustomizationMinPermittedException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter Customization quantity min permitted"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleCustomizationNotFound() {
        val ex = CustomizationNotFoundException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "No Customization founded with the specified Id in the Offer"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleCustomizationOptionsIsEmpty() {
        val ex = CustomizationOptionsIsEmptyException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid state Customization Options is empty"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleDescriptionLength() {
        val ex = DescriptionLengthException("12")
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter length Description"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleDuplicatedCustomization() {
        val ex = DuplicatedCustomizationException(listOf(Id()))
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid state duplicated Customization Id in the Offer"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleDuplicatedOption() {
        val ex = DuplicatedOptionException(listOf(Id()))
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid state duplicated Option Id in the Offer"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleMalformedImagePathURL() {
        val ex = MalformedImagePathURLException("dsa")
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter Image path is not a valid URL"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleNameLength() {
        val ex = NameLengthException("ds")
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter length Name"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleOfferPriceZero() {
        val ex = OfferPriceZeroException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter the Offer price cannot be zero"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleOptionAlreadyExists() {
        val ex = OptionAlreadyExistsException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.CONFLICT

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.CONFLICT.value()
        problemDetail.title shouldBe "Already exists a Option with the specified Id in the Offer"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleOptionNotFound() {
        val ex = OptionNotFoundException(Id())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.NOT_FOUND

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.NOT_FOUND.value()
        problemDetail.title shouldBe "No Option founded with the specified Id in the Offer"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handlePriceNegative() {
        val ex = PriceNegativeException("-10".toBigDecimal())
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter Price negative"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleQuantityMaxPermitted() {
        val ex = QuantityMaxPermittedException(3, 1)
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter Quantity Max Permitted"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleQuantityMaxPermittedZero() {
        val ex = QuantityMaxPermittedZeroException(0)
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter Quantity Max Permitted is Zero"
        problemDetail.detail shouldBe ex.message
    }

    @Test
    fun handleQuantityMinPermittedException() {
        val ex = QuantityMinPermittedException(-1)
        val result = handler.exceptionHandler(ex)

        result.shouldBeTypeOf<ResponseEntity<ProblemDetail>>()
        result.statusCode shouldBe HttpStatus.BAD_REQUEST

        val problemDetail = result.body

        problemDetail.shouldNotBeNull()
        problemDetail.status shouldBe HttpStatus.BAD_REQUEST.value()
        problemDetail.title shouldBe "Invalid parameter Quantity Min Permitted"
        problemDetail.detail shouldBe ex.message
    }
}
