package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Option
import com.github.michaelbull.result.Result
import java.util.UUID

interface OptionInputPort {
    suspend fun create(option: Option): Result<Unit, Throwable>
    suspend fun update(option: Option): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable>
    suspend fun patch(option: Option): Result<Unit, Throwable>
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Option>, Throwable>
}
