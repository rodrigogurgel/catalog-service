package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.option

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateOptionEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class CreateOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<CreateOptionEventRequest> {

    @Timed("create.option.event")
    override suspend fun process(record: CreateOptionEventRequest): Result<Unit, Throwable> =
        optionInputPort.create(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateOptionEventRequest
    }
}
