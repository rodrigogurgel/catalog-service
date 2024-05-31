package br.com.rodrigogurgel.catalogservice.adapter.out.events.producer

import br.com.rodrigogurgel.catalogservice.application.exception.out.events.NotifyEventResponseException
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CatalogEventOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.future.await
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CatalogProducer(
    private val kafkaTemplate: KafkaTemplate<String, GenericRecord>,
) : CatalogEventOutputPort {

    override suspend fun notifyResponse(record: GenericRecord): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val producerRecord = ProducerRecord(
            "catalog-output",
            null,
            null,
            UUID.randomUUID().toString(),
            record,
            null
        )

        kafkaTemplate.send(producerRecord).await()
    }.mapError { NotifyEventResponseException(record) }

    override suspend fun notifyRequest(record: GenericRecord): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val producerRecord = ProducerRecord(
            "catalog-input",
            null,
            null,
            UUID.randomUUID().toString(),
            record,
            null
        )

        kafkaTemplate.send(producerRecord).await()
    }.mapError { RuntimeException() }
}