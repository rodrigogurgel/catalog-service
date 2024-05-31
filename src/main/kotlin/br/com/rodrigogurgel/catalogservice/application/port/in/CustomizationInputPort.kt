package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result
import java.util.UUID

interface CustomizationInputPort {
    suspend fun create(transaction: Transaction<Customization>): Result<Unit, Throwable>
    suspend fun update(transaction: Transaction<Customization>): Result<Unit, Throwable>
    suspend fun delete(transaction: Transaction<Customization>, cascade: Boolean = true): Result<Unit, Throwable>
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Customization>, Throwable>
}
