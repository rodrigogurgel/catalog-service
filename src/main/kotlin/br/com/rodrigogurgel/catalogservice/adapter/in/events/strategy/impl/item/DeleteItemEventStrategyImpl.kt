package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.item

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteItemEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteItemEventStrategyImpl(
    private val itemInputPort: ItemInputPort,
) : GenericRecordEventStrategy<DeleteItemEventDTO> {
    @Timed("delete.item.event")
    override suspend fun process(record: DeleteItemEventDTO): Result<Unit, Throwable> =
        itemInputPort.delete(record.storeId.toString().toUUID(), record.itemId.toString().toUUID())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteItemEventDTO
    }
}
