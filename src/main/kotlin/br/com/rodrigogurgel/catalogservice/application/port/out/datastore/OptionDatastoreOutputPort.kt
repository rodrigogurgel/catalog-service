package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Option
import java.util.UUID

interface OptionDatastoreOutputPort {
    suspend fun create(option: Option)
    suspend fun update(option: Option)
    suspend fun delete(storeId: UUID, optionId: UUID)
    suspend fun patch(option: Option)
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Option>
}