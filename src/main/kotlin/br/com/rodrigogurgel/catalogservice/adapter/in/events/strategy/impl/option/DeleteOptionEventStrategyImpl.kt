package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.option

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteOptionEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<DeleteOptionEventRequest> {

    @Timed("delete.option.event")
    override suspend fun process(record: DeleteOptionEventRequest): Result<Unit, Throwable> =
        optionInputPort.delete(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteOptionEventRequest
    }
}
