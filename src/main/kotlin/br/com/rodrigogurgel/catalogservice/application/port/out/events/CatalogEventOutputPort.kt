package br.com.rodrigogurgel.catalogservice.application.port.out.events

import com.github.michaelbull.result.Result
import org.apache.avro.generic.GenericRecord

interface CatalogEventOutputPort {
    suspend fun notifyResponse(record: GenericRecord): Result<Unit, Throwable>
    suspend fun notifyRequest(record: GenericRecord): Result<Unit, Throwable>
}
