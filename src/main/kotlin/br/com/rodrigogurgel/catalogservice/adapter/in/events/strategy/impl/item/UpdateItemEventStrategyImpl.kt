package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.item

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateItemEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UpdateItemEventStrategyImpl(
    private val itemInputPort: ItemInputPort,
) : GenericRecordEventStrategy<UpdateItemEventDTO> {
    @Timed("update.item.event")
    override suspend fun process(idempotencyId: UUID, correlationId: UUID, record: UpdateItemEventDTO): Result<Unit, Throwable> =
        itemInputPort.update(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateItemEventDTO
    }
}
