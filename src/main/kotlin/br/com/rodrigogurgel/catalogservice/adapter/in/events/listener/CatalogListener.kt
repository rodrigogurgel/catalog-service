package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.listener

import br.com.rodrigogurgel.catalogservice.adapter.`in`.events.strategy.GenericRecordEventStrategy
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.exception.`in`.events.UnsupportedRecordTypeException
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.onFailure
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.system.measureTimeMillis

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
                runSuspendCatching {
                    val record = consumerRecord.value()
                    val headers = consumerRecord.headers().associate { it.key() to it.value() }
                    val correlationId = headers["correlationId"]?.toString()?.toUUID() ?: UUID.randomUUID()

                    val strategy = processors
                        .firstOrNull { it.canProcess(record) }
                        ?: throw UnsupportedRecordTypeException(record)

                    val processTimeMs = measureTimeMillis {
                        strategy.process(consumerRecord.key().toUUID(), correlationId, record)
                    }
                    logger.info("Process time: $processTimeMs to strategy ${strategy::class.simpleName}")
                }.onFailure { logger.error("Error while processing record", it) }
            }
        }

        acknowledgment.acknowledge()
    }
}
