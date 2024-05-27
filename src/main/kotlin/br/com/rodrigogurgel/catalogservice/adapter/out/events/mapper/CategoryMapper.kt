package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper

import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CategoryDeletedDTO
import br.com.rodrigogurgel.catalogservice.out.events.dto.CategoryCreatedDTO
import br.com.rodrigogurgel.catalogservice.out.events.dto.CategoryPatchedDTO
import br.com.rodrigogurgel.catalogservice.out.events.dto.CategoryUpdatedDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("CategoryMapper")

fun Category.toCategoryCreatedDTO(): Result<GenericRecord, Throwable> = runCatching {
    CategoryCreatedDTO
        .newBuilder()
        .setCategoryId(categoryId.toString())
        .setName(name)
        .setStoreId(storeId.toString())
        .setStatus(status!!.toCategoryCreatedDTO())
        .setIndex(index!!)
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}

fun Category.toCategoryUpdatedDTO(): Result<GenericRecord, Throwable> = runCatching {
    CategoryUpdatedDTO
        .newBuilder()
        .setCategoryId(categoryId.toString())
        .setName(name)
        .setStoreId(storeId.toString())
        .setStatus(status!!.toCategoryCreatedDTO())
        .setIndex(index!!)
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}

fun Category.toCategoryDeletedDTO(): Result<GenericRecord, Throwable> = runCatching {
    CategoryDeletedDTO
        .newBuilder()
        .setCategoryId(categoryId.toString())
        .setStoreId(storeId.toString())
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}

fun Category.toCategoryPatchedDTO(): Result<GenericRecord, Throwable> = runCatching {
    CategoryPatchedDTO
        .newBuilder()
        .setCategoryId(categoryId.toString())
        .setName(name)
        .setStoreId(storeId!!.toString())
        .setStatus(status?.toCategoryCreatedDTO())
        .setIndex(index)
        .build()
}.onFailure {
    logger.error(it.message, it)
    throw it
}