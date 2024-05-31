package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.item

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateItemEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class CreateItemEventStrategyImpl(
    private val itemInputPort: ItemInputPort,
) : GenericRecordEventStrategy<CreateItemEventRequest> {

    @Timed("create.item.event")
    override suspend fun process(record: CreateItemEventRequest): Result<Unit, Throwable> =
        itemInputPort.create(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateItemEventRequest
    }
}
