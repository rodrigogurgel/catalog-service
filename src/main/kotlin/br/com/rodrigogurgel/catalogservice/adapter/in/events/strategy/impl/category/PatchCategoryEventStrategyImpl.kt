package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.category

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchCategoryEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PatchCategoryEventStrategyImpl(
    private val categoryInputPort: CategoryInputPort,
) : GenericRecordEventStrategy<PatchCategoryEventDTO> {
    @Timed("patch.category.event")
    override suspend fun process(
        idempotencyId: UUID,
        correlationId: UUID,
        record: PatchCategoryEventDTO,
    ): Result<Unit, Throwable> =
        categoryInputPort.patch(idempotencyId, correlationId, record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is PatchCategoryEventDTO
    }
}
