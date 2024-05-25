package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper

import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.out.events.dto.ProductCreatedDTO
import br.com.rodrigogurgel.catalogservice.out.events.dto.ProductDeletedDTO
import br.com.rodrigogurgel.catalogservice.out.events.dto.ProductPatchedDTO
import br.com.rodrigogurgel.catalogservice.out.events.dto.ProductUpdatedDTO
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.github.michaelbull.result.Result
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ProductMapper")

fun Product.toProductCreatedDTO(): Result<GenericRecord, Throwable> = runCatching {
    ProductCreatedDTO
        .newBuilder()
        .setProductId(productId!!.toString())
        .setName(name!!)
        .setImage(image)
        .setDescription(description)
        .setStoreId(storeId!!.toString())
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}

fun Product.toProductUpdatedDTO(): Result<GenericRecord, Throwable> = runCatching {
    ProductUpdatedDTO
        .newBuilder()
        .setProductId(productId!!.toString())
        .setName(name!!)
        .setImage(image)
        .setDescription(description)
        .setStoreId(storeId!!.toString())
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}

fun Product.toProductDeletedDTO(): Result<GenericRecord, Throwable> = runCatching {
    ProductDeletedDTO
        .newBuilder()
        .setProductId(productId!!.toString())
        .setStoreId(storeId!!.toString())
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}

fun Product.toProductPatchedDTO(): Result<GenericRecord, Throwable> = runCatching {
    ProductPatchedDTO
        .newBuilder()
        .setProductId(productId!!.toString())
        .setStoreId(storeId!!.toString())
        .setName(name)
        .setImage(image)
        .setDescription(description)
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}