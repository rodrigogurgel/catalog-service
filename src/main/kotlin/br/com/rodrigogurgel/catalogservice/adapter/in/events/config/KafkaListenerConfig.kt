package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.config

import io.micrometer.core.instrument.MeterRegistry
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.MicrometerConsumerListener
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaListenerConfig(
    private val properties: KafkaProperties,
    private val meterRegistry: MeterRegistry,
) {
    private val logger = LoggerFactory.getLogger(KafkaListenerConfig::class.java)

    private val defaultErrorHandler = DefaultErrorHandler(
        { _, error ->
            logger.error("Some error happened", error)
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
        containerFactory.setConcurrency(10)
        containerFactory.isBatchListener = true
        containerFactory.setRecordFilterStrategy { false }
        return containerFactory
    }

    @Bean
    fun catalogContainerFactory(
        consumerFactory: ConsumerFactory<String, GenericRecord>,
    ): ConcurrentKafkaListenerContainerFactory<String, GenericRecord> {
        return buildContainerFactory()
    }
}