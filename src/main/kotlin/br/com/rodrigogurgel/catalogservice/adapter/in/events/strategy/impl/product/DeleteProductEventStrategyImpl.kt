package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.product

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteProductEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteProductEventStrategyImpl(
    private val productInputPort: ProductInputPort,
) : GenericRecordEventStrategy<DeleteProductEventRequest> {

    @Timed("delete.product.event")
    override suspend fun process(record: DeleteProductEventRequest): Result<Unit, Throwable> =
        productInputPort.delete(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteProductEventRequest
    }
}
