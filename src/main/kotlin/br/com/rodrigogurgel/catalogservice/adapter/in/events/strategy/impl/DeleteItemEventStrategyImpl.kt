package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteItemEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteItemEventStrategyImpl(
    private val itemInputPort: ItemInputPort,
) : GenericRecordEventStrategy<DeleteItemEventDTO> {
    override suspend fun process(record: DeleteItemEventDTO): Result<Unit, Throwable> = runCatching {
        itemInputPort.delete(record.storeId.toString().toUUID(), record.itemId.toString().toUUID())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteItemEventDTO
    }
}