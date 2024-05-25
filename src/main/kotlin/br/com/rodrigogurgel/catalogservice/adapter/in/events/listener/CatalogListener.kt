package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.listener

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import kotlin.system.measureTimeMillis
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
    fun onMessage(consumerRecords: List<ConsumerRecord<String, GenericRecord>>, acknowledgment: Acknowledgment) {
        val processTimeMs = measureTimeMillis {
            consumerRecords.parallelStream().forEach { consumerRecord ->
                runCatching {
                    val record = consumerRecord.value()

                    val strategy = processors
                        .firstOrNull { it.canProcess(record) }
                        ?: throw IllegalArgumentException("No processor found for record: ${record::class.qualifiedName}")

                    runBlocking {
                        strategy.process(record)
                    }
                }.onFailure { logger.error("Error while processing record", it) }
            }
        }
        logger.info("Process time: $processTimeMs")

        acknowledgment.acknowledge()
    }
}