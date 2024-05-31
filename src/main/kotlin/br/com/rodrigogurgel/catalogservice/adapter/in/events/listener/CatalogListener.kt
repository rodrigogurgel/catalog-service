package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.listener

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.exception.`in`.events.UnsupportedRecordTypeException
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class CatalogListener(
    private val processors: List<GenericRecordEventStrategy<GenericRecord>>,
) {
    private val logger = LoggerFactory.getLogger(CatalogListener::class.java)

    @KafkaListener(
        id = "catalog-service",
        groupId = "catalog-service",
        idIsGroup = false,
        autoStartup = "true",
        topics = ["catalog-input"],
        containerFactory = "catalogContainerFactory"
    )
    fun onMessage(
        consumerRecords: List<ConsumerRecord<String, GenericRecord>>,
        acknowledgment: Acknowledgment,
    ) {
        consumerRecords.parallelStream().forEach { consumerRecord ->
            runBlocking {
                val record = consumerRecord.value()

                val strategy = processors
                    .firstOrNull { it.canProcess(record) }
                    ?: throw UnsupportedRecordTypeException(record)

                strategy.process(record)
                    .onFailure { logger.error("Strategy ${strategy::class.simpleName} executed with failure", it) }
                    .onSuccess { logger.info("Strategy ${strategy::class.simpleName} executed successfully") }
            }
        }

        acknowledgment.acknowledge()
    }
}
