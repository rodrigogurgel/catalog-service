package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.config

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.domain.Idempotency
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.MicrometerConsumerListener
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff
import java.util.UUID

@Configuration
class KafkaListenerConfig(
    @Value("\${catalog.consumer.concurrency:1}")
    private val concurrency: Int,
    private val properties: KafkaProperties,
    private val meterRegistry: MeterRegistry,
    private val idempotencyInputPort: IdempotencyInputPort,
) {
    private val logger = LoggerFactory.getLogger(KafkaListenerConfig::class.java)

    private val defaultErrorHandler = DefaultErrorHandler(
        { consumerRecord, error ->
            val record = runCatching { consumerRecord.value() }.getOrNull() ?: return@DefaultErrorHandler
            val headers = consumerRecord.headers().associate { it.key() to it.value() }

            logger.error(
                "An unexpected error happened while try process record type [${record::class.qualifiedName}]",
                error
            )

            val idempotencyId =
                runCatching { consumerRecord.key().toString().toUUID() }.getOrNull() ?: return@DefaultErrorHandler
            val correlationId = headers["correlationId"]?.toUUID() ?: UUID.randomUUID()

            runBlocking {
                idempotencyInputPort.patch(Idempotency.failure(idempotencyId, correlationId))
            }
        },
        FixedBackOff(0L, 0L),
    ).apply { isAckAfterHandle = true }

    private fun buildContainerFactory(
        configs: Map<String, Any> = properties.consumer.properties + mapOf(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to properties.consumer.keyDeserializer,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to properties.consumer.valueDeserializer,
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.bootstrapServers,
        ),
    ): ConcurrentKafkaListenerContainerFactory<String, GenericRecord> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, GenericRecord>()

        containerFactory.consumerFactory = DefaultKafkaConsumerFactory<String, GenericRecord>(configs)
            .apply { addListener(MicrometerConsumerListener(meterRegistry)) }

        containerFactory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        containerFactory.setAckDiscarded(true)
        containerFactory.setCommonErrorHandler(defaultErrorHandler)
        containerFactory.setConcurrency(concurrency)
        containerFactory.isBatchListener = true
        containerFactory.setRecordFilterStrategy { false }
        return containerFactory
    }

    @Bean
    fun catalogContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GenericRecord> {
        return buildContainerFactory()
    }
}
