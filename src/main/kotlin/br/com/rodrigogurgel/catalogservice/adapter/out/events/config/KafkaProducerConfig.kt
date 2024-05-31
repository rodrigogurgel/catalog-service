package br.com.rodrigogurgel.catalogservice.adapter.out.events.config

import org.apache.avro.generic.GenericRecord
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfig {

    @Bean
    fun kafkaTemplate(
        producerFactory: ProducerFactory<String, GenericRecord>,
    ): KafkaTemplate<String, GenericRecord> {
        return KafkaTemplate(producerFactory)
    }
}
