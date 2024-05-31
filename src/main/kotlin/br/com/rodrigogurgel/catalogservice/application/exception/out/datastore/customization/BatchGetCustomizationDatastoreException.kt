package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization

import software.amazon.awssdk.enhanced.dynamodb.Key

data class BatchGetCustomizationDatastoreException(val keys: Set<Key>, override val cause: Throwable) :
    RuntimeException("Failed to get batch customizations with keys: [$keys]", cause)
