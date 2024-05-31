package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.item

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteItemEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteItemEventStrategyImpl(
    private val itemInputPort: ItemInputPort,
) : GenericRecordEventStrategy<DeleteItemEventRequest> {

    @Timed("delete.item.event")
    override suspend fun process(record: DeleteItemEventRequest): Result<Unit, Throwable> =
        itemInputPort.delete(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteItemEventRequest
    }
}
