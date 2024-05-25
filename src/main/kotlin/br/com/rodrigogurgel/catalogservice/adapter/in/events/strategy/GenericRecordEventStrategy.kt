package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy

import org.apache.avro.generic.GenericRecord
import com.github.michaelbull.result.Result

interface GenericRecordEventStrategy<out T : GenericRecord> {
    suspend fun process(record: @UnsafeVariance T): Result<Unit, Throwable>
    fun canProcess(record: GenericRecord): Boolean
}