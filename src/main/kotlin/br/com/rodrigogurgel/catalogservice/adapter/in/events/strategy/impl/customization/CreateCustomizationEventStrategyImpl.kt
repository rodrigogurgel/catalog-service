package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.customization

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateCustomizationEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class CreateCustomizationEventStrategyImpl(
    private val customizationInputPort: CustomizationInputPort,
) : GenericRecordEventStrategy<CreateCustomizationEventRequest> {

    @Timed("create.customization.event")
    override suspend fun process(record: CreateCustomizationEventRequest): Result<Unit, Throwable> =
        customizationInputPort.create(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is CreateCustomizationEventRequest
    }
}
