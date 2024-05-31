package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result
import java.util.UUID

interface OptionInputPort {
    suspend fun create(transaction: Transaction<Option>): Result<Unit, Throwable>
    suspend fun update(transaction: Transaction<Option>): Result<Unit, Throwable>
    suspend fun delete(transaction: Transaction<Option>, cascade: Boolean = true): Result<Unit, Throwable>
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Option>, Throwable>
    suspend fun searchByProductId(storeId: UUID, productId: UUID): Result<List<Option>, Throwable>
}
