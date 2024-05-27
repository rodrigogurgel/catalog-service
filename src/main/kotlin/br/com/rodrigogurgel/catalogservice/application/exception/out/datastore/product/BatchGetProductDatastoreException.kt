package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.product

import software.amazon.awssdk.enhanced.dynamodb.Key

data class BatchGetProductDatastoreException(val keys: Set<Key>, override val cause: Throwable) :
    RuntimeException("Failed to get batch products with keys: [$keys]", cause)
