package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.customization

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateCustomizationEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UpdateCustomizationEventStrategyImpl(
    private val customizationInputPort: CustomizationInputPort,
) : GenericRecordEventStrategy<UpdateCustomizationEventDTO> {
    @Timed("update.customization.event")
    override suspend fun process(
        idempotencyId: UUID,
        correlationId: UUID,
        record: UpdateCustomizationEventDTO,
    ): Result<Unit, Throwable> =
        customizationInputPort.update(idempotencyId, correlationId, record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateCustomizationEventDTO
    }
}
