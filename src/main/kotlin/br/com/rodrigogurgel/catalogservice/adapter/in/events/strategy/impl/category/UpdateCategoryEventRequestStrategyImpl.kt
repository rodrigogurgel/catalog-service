package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.category

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateCategoryEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class UpdateCategoryEventRequestStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<UpdateCategoryEventRequest> {

    @Timed("update.category.event")
    override suspend fun process(record: UpdateCategoryEventRequest): Result<Unit, Throwable> =
        categoryInputPort.update(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateCategoryEventRequest
    }
}
