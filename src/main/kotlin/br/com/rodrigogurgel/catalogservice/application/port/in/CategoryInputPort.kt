package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result

interface CategoryInputPort {
    suspend fun create(transaction: Transaction<Category>): Result<Unit, Throwable>
    suspend fun update(transaction: Transaction<Category>): Result<Unit, Throwable>
    suspend fun delete(transaction: Transaction<Category>): Result<Unit, Throwable>
}
