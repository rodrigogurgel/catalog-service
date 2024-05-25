package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.impl

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateProductEventDTO
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.springframework.stereotype.Component

@Component
class UpdateProductEventStrategyImpl(
    private val productInputPort: ProductInputPort,
) : GenericRecordEventStrategy<UpdateProductEventDTO> {
    override suspend fun process(record: UpdateProductEventDTO): Result<Unit, Throwable> = runCatching {
        productInputPort.update(record.toDomain())
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is UpdateProductEventDTO
    }
}