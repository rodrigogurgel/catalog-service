package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateOptionEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class UpdateOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<UpdateOptionEventDTO> {
    override suspend fun process(record: UpdateOptionEventDTO): Result<Unit, Throwable> = runCatching {
        optionInputPort.update(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateOptionEventDTO
    }
}