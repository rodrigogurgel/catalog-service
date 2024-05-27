package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option

import software.amazon.awssdk.enhanced.dynamodb.Key

data class BatchGetOptionDatastoreException(val keys: Set<Key>) :
    RuntimeException("Failed to get batch options with keys: [$keys]")
