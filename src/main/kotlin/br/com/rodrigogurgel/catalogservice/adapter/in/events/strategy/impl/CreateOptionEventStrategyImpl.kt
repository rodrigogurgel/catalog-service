package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateOptionEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class CreateOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<CreateOptionEventDTO> {
    override suspend fun process(record: CreateOptionEventDTO): Result<Unit, Throwable> = runCatching {
        optionInputPort.create(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateOptionEventDTO
    }
}