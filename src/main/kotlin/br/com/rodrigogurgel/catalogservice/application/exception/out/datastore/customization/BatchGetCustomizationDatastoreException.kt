package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization

import software.amazon.awssdk.enhanced.dynamodb.Key

data class BatchGetCustomizationDatastoreException(val keys: Set<Key>) :
    RuntimeException("Failed to get batch customizations with keys: [$keys]")
