package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.config

import io.micrometer.core.instrument.MeterRegistry
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.ssl.DefaultSslBundleRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.MicrometerConsumerListener
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaListenerConfig(
    @Value("\${catalog.consumer.concurrency:1}")
    private val concurrency: Int,
    private val properties: KafkaProperties,
    private val meterRegistry: MeterRegistry,
) {
    private val logger = LoggerFactory.getLogger(KafkaListenerConfig::class.java)

    private val defaultErrorHandler = DefaultErrorHandler(
        { consumerRecord, error ->
            val record = runCatching { consumerRecord.value() }.getOrElse {
                logger.error(
                    "An unexpected error happened while try process [$consumerRecord]",
                    error
                )
                null
            } ?: return@DefaultErrorHandler

            logger.error(
                "An unexpected error happened while try process record type [${record::class.qualifiedName}]",
                error
            )
        },
        FixedBackOff(0L, 0L),
    ).apply { isAckAfterHandle = true }

    private fun buildContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, GenericRecord> {

        val configs = properties.buildConsumerProperties(DefaultSslBundleRegistry())
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
