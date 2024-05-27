package br.com.rodrigogurgel.catalogservice.adapter.out.events.producer

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GenericOutputProducer(
    @Value("\${catalog.output-topic}")
    private val catalogOutputTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, GenericRecord>,
) {
    private val logger = LoggerFactory.getLogger(GenericOutputProducer::class.java)

    fun send(transactionId: UUID, record: GenericRecord): Result<Unit, Throwable> = runCatching<Unit> {
        val producerRecord = ProducerRecord(catalogOutputTopic, null, null, "$transactionId", record, null)

        kafkaTemplate.send(producerRecord).whenComplete { _, error ->
            if (error != null) throw error
        }
    }.onFailure {
        logger.error(it.message, it)
        throw it
    }
}
