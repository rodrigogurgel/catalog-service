package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.transaction

import java.util.UUID

data class TransactionAlreadyExistsException(val transactionId: UUID) : RuntimeException(
    "Transaction $transactionId already exists"
)
