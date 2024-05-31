package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.customization

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteCustomizationEventRequest
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteCustomizationEventStrategyImpl(
    private val customizationInputPort: CustomizationInputPort,
) : GenericRecordEventStrategy<DeleteCustomizationEventRequest> {

    @Timed("delete.customization.event")
    override suspend fun process(record: DeleteCustomizationEventRequest): Result<Unit, Throwable> =
        customizationInputPort.delete(record.toDomain())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteCustomizationEventRequest
    }
}
