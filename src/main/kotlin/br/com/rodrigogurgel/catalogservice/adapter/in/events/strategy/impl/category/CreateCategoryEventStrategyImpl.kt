package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.category

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateCategoryEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class CreateCategoryEventStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<CreateCategoryEventDTO> {
    @Timed("create.category.event")
    override suspend fun process(record: CreateCategoryEventDTO): Result<Unit, Throwable> =
        categoryInputPort.create(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateCategoryEventDTO
    }
}
