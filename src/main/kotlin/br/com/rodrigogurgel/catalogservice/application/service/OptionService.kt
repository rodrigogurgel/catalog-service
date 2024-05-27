package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.OptionDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Option
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OptionService(
    private val optionDatastoreOutputPort: OptionDatastoreOutputPort,
) : OptionInputPort {
    override suspend fun create(option: Option): Result<Unit, Throwable> = optionDatastoreOutputPort.create(option)

    override suspend fun update(option: Option): Result<Unit, Throwable> = optionDatastoreOutputPort.update(option)

    override suspend fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable> =
        optionDatastoreOutputPort.delete(storeId, optionId)

    override suspend fun patch(option: Option): Result<Unit, Throwable> = optionDatastoreOutputPort.patch(option)

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Option>, Throwable> = optionDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
}
