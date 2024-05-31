package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Customization
import com.github.michaelbull.result.Result
import java.util.UUID

interface CustomizationDatastoreOutputPort {
    suspend fun create(customization: Customization): Result<Unit, Throwable>
    suspend fun update(customization: Customization): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, customizationId: UUID): Result<Unit, Throwable>
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Customization>, Throwable>
}
