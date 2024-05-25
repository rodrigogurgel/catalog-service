package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateItemEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class CreateItemEventStrategyImpl(
    private val itemInputPort: ItemInputPort,
) : GenericRecordEventStrategy<CreateItemEventDTO> {
    override suspend fun process(record: CreateItemEventDTO): Result<Unit, Throwable> = runCatching {
        itemInputPort.create(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateItemEventDTO
    }
}