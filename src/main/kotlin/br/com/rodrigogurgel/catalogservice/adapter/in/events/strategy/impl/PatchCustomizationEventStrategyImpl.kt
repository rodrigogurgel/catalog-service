package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchCustomizationEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class PatchCustomizationEventStrategyImpl(
    private val customizationInputPort: CustomizationInputPort,
) : GenericRecordEventStrategy<PatchCustomizationEventDTO> {
    override suspend fun process(record: PatchCustomizationEventDTO): Result<Unit, Throwable> = runCatching {
        customizationInputPort.patch(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is PatchCustomizationEventDTO
    }
}