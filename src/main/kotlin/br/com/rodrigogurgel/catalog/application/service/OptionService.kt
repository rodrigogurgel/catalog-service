package br.com.rodrigogurgel.catalog.application.service

import br.com.rodrigogurgel.catalog.domain.Option
import br.com.rodrigogurgel.catalog.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalog.application.port.out.datastore.OptionDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class OptionService(
    private val optionDatastoreOutputPort: OptionDatastoreOutputPort,
) : OptionInputPort {

    override fun create(option: Option): Result<Unit, Throwable> = runCatching {
        optionDatastoreOutputPort.create(option)
    }

    override fun update(option: Option): Result<Unit, Throwable> = runCatching {
        optionDatastoreOutputPort.update(option)
    }

    override fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable> = runCatching {
        optionDatastoreOutputPort.delete(storeId, optionId)
    }

    override fun patch(option: Option): Result<Unit, Throwable> = runCatching {
        optionDatastoreOutputPort.patch(option)
    }

    override fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Option>, Throwable> = runCatching {
        optionDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
    }
}