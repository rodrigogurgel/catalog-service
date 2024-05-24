package br.com.rodrigogurgel.catalog.application.port.out.datastore

import br.com.rodrigogurgel.catalog.domain.Option
import java.util.UUID

interface OptionDatastoreOutputPort {
    fun create(option: Option)
    fun update(option: Option)
    fun delete(storeId: UUID, optionId: UUID)
    fun patch(option: Option)
    fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Option>
}