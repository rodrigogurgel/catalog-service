package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.option

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateOptionEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class UpdateOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<UpdateOptionEventDTO> {
    @Timed("update.option.event")
    override suspend fun process(record: UpdateOptionEventDTO): Result<Unit, Throwable> =
        optionInputPort.update(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateOptionEventDTO
    }
}