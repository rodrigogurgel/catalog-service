package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.option

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchOptionEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PatchOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<PatchOptionEventDTO> {
    @Timed("patch.option.event")
    override suspend fun process(idempotencyId: UUID, correlationId: UUID, record: PatchOptionEventDTO): Result<Unit, Throwable> =
        optionInputPort.patch(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is PatchOptionEventDTO
    }
}
