package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteProductEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteProductEventStrategyImpl(
    private val productInputPort: ProductInputPort,
) : GenericRecordEventStrategy<DeleteProductEventDTO> {
    override suspend fun process(record: DeleteProductEventDTO): Result<Unit, Throwable> = runCatching {
        productInputPort.delete(record.storeId.toString().toUUID(), record.productId.toString().toUUID())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteProductEventDTO
    }
}