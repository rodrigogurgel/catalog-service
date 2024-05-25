package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateCategoryEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class UpdateCategoryEventStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<UpdateCategoryEventDTO> {
    override suspend fun process(record: UpdateCategoryEventDTO): Result<Unit, Throwable> = runCatching {
        categoryInputPort.update(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateCategoryEventDTO
    }
}