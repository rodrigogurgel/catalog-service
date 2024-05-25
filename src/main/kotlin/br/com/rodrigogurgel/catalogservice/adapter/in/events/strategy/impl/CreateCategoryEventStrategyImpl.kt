package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateCategoryEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CreateCategoryEventStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<CreateCategoryEventDTO> {
    override suspend fun process(record: CreateCategoryEventDTO): Result<Unit, Throwable> = runCatching {
        categoryInputPort.create(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateCategoryEventDTO
    }
}