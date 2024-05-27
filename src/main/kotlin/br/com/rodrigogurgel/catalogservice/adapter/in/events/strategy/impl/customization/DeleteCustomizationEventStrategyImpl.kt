package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl.customization

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteCustomizationEventDTO
import com.github.michaelbull.result.Result
import io.micrometer.core.annotation.Timed
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteCustomizationEventStrategyImpl(
    private val customizationInputPort: CustomizationInputPort,
) : GenericRecordEventStrategy<DeleteCustomizationEventDTO> {
    @Timed("delete.customization.event")
    override suspend fun process(record: DeleteCustomizationEventDTO): Result<Unit, Throwable> =
        customizationInputPort.delete(record.storeId.toString().toUUID(), record.customizationId.toString().toUUID())

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteCustomizationEventDTO
    }
}
