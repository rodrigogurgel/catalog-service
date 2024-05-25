package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateCustomizationEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class UpdateCustomizationEventStrategyImpl(
    private val customizationInputPort: CustomizationInputPort,
) : GenericRecordEventStrategy<UpdateCustomizationEventDTO> {
    override suspend fun process(record: UpdateCustomizationEventDTO): Result<Unit, Throwable> = runCatching {
        customizationInputPort.update(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateCustomizationEventDTO
    }
}