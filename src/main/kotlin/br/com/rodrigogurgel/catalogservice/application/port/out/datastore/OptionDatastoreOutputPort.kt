package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Option
import com.github.michaelbull.result.Result
import java.util.UUID

interface OptionDatastoreOutputPort {
    suspend fun create(option: Option): Result<Unit, Throwable>
    suspend fun update(option: Option): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable>
    suspend fun patch(option: Option): Result<Unit, Throwable>
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Option>, Throwable>
}
