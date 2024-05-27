package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.product

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateProductEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateProductEventStrategyImpl(
    private val productInputPort: ProductInputPort,
) : GenericRecordEventStrategy<CreateProductEventDTO> {
    @Timed("create.product.event")
    override suspend fun process(
        idempotencyId: UUID,
        correlationId: UUID,
        record: CreateProductEventDTO,
    ): Result<Unit, Throwable> =
        productInputPort.create(idempotencyId, correlationId, record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateProductEventDTO
    }
}
