package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.category

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteCategoryEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteCategoryEventStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<DeleteCategoryEventDTO> {
    @Timed("delete.category.event")
    override suspend fun process(record: DeleteCategoryEventDTO): Result<Unit, Throwable> =
        categoryInputPort.delete(record.storeId.toString().toUUID(), record.categoryId.toString().toUUID())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteCategoryEventDTO
    }
}
