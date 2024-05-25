package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.OptionDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.onFailure
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class OptionService(
    private val optionDatastoreOutputPort: OptionDatastoreOutputPort,
) : OptionInputPort {
    override suspend fun create(option: Option): Result<Unit, Throwable> = runSuspendCatching {
        optionDatastoreOutputPort.create(option)
    }.onFailure {
        throw it
    }

    override suspend fun update(option: Option): Result<Unit, Throwable> = runSuspendCatching {
        optionDatastoreOutputPort.update(option)
    }.onFailure {
        throw it
    }

    override suspend fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable> = runSuspendCatching {
        optionDatastoreOutputPort.delete(storeId, optionId)
    }.onFailure {
        throw it
    }

    override suspend fun patch(option: Option): Result<Unit, Throwable> = runSuspendCatching {
        optionDatastoreOutputPort.patch(option)
    }.onFailure {
        throw it
    }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Option>, Throwable> = runSuspendCatching {
        optionDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
    }.onFailure {
        throw it
    }
}