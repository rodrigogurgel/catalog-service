package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteOptionEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class DeleteOptionEventStrategyImpl(
    private val optionInputPort: OptionInputPort,
) : GenericRecordEventStrategy<DeleteOptionEventDTO> {
    override suspend fun process(record: DeleteOptionEventDTO): Result<Unit, Throwable> = runCatching {
        optionInputPort.delete(record.storeId.toString().toUUID(), record.optionId.toString().toUUID())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is DeleteOptionEventDTO
    }
}