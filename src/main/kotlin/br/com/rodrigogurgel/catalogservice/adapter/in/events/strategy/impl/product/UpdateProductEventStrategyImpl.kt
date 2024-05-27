package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.product

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateProductEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UpdateProductEventStrategyImpl(
    private val productInputPort: ProductInputPort,
) : GenericRecordEventStrategy<UpdateProductEventDTO> {
    @Timed("update.product.event")
    override suspend fun process(
        idempotencyId: UUID,
        correlationId: UUID,
        record: UpdateProductEventDTO,
    ): Result<Unit, Throwable> =
        productInputPort.update(idempotencyId, correlationId, record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateProductEventDTO
    }
}
