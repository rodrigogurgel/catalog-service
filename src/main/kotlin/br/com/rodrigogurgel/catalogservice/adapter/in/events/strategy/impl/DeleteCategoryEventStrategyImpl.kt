package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteCategoryEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteCategoryEventStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<DeleteCategoryEventDTO> {
    override suspend fun process(record: DeleteCategoryEventDTO): Result<Unit, Throwable> = runCatching {
        categoryInputPort.delete(record.storeId.toString().toUUID(), record.categoryId.toString().toUUID())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteCategoryEventDTO
    }
}