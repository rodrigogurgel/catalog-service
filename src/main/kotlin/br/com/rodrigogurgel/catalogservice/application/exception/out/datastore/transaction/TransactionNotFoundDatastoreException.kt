package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.transaction

import java.util.UUID

data class TransactionNotFoundDatastoreException(val transactionId: UUID) :
    RuntimeException("Transaction $transactionId not found")
