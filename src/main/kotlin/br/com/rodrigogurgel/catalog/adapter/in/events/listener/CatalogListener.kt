package br.com.rodrigogurgel.catalog.adapter.`in`.events.listener

import br.com.rodrigogurgel.catalog.adapter.`in`.events.mapper.toDomain
import br.com.rodrigogurgel.catalog.application.common.toUUID
import br.com.rodrigogurgel.catalog.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalog.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalog.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalog.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalog.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateCategoryEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateCustomizationEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateItemEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateOptionEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateProductEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.DeleteCategoryEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.DeleteCustomizationEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.DeleteItemEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.DeleteOptionEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.DeleteProductEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchCategoryEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchCustomizationEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchItemEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchOptionEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchProductEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateCategoryEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateCustomizationEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateItemEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateOptionEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateProductEventDTO
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class CatalogListener(
    private val categoryInputPort: CategoryInputPort,
    private val productInputPort: ProductInputPort,
    private val customizationInputPort: CustomizationInputPort,
    private val itemInputPort: ItemInputPort,
    private val optionInputPort: OptionInputPort,
) {
    private val logger = LoggerFactory.getLogger(CatalogListener::class.java)

    @KafkaListener(
        id = "catalog-listener",
        groupId = "catalog-listener",
        idIsGroup = false,
        autoStartup = "true",
        topics = ["catalog-input"],
        containerFactory = "catalogContainerFactory"
    )
    fun onMessage(consumerRecords: List<ConsumerRecord<String, GenericRecord>>, acknowledgment: Acknowledgment) {
        consumerRecords.parallelStream().forEach { consumerRecord ->
            val record = consumerRecord.value()

            runCatching {
                when (record) {
                    is CreateCategoryEventDTO -> categoryInputPort.create(
                        category = record.toDomain()
                    )

                    is DeleteCategoryEventDTO -> categoryInputPort.delete(
                        storeId = record.storeId.toString().toUUID(),
                        categoryId = record.categoryId.toString().toUUID()
                    )

                    is UpdateCategoryEventDTO -> categoryInputPort.update(
                        category = record.toDomain()
                    )

                    is PatchCategoryEventDTO -> categoryInputPort.patch(
                        category = record.toDomain()
                    )

                    is CreateProductEventDTO -> productInputPort.create(
                        product = record.toDomain()
                    )

                    is DeleteProductEventDTO -> productInputPort.delete(
                        storeId = record.storeId.toString().toUUID(),
                        productId = record.productId.toString().toUUID()
                    )

                    is UpdateProductEventDTO -> productInputPort.update(
                        product = record.toDomain()
                    )

                    is PatchProductEventDTO -> productInputPort.update(
                        product = record.toDomain()
                    )

                    is CreateItemEventDTO -> itemInputPort.create(
                        item = record.toDomain()
                    )

                    is DeleteItemEventDTO -> itemInputPort.delete(
                        storeId = record.storeId.toString().toUUID(),
                        itemId = record.itemId.toString().toUUID()
                    )

                    is UpdateItemEventDTO -> itemInputPort.update(
                        item = record.toDomain()
                    )

                    is PatchItemEventDTO -> itemInputPort.patch(
                        item = record.toDomain()
                    )

                    is CreateCustomizationEventDTO -> customizationInputPort.create(
                        customization = record.toDomain()
                    )

                    is DeleteCustomizationEventDTO -> customizationInputPort.delete(
                        storeId = record.storeId.toString().toUUID(),
                        customizationId = record.customizationId.toString().toUUID()
                    )

                    is UpdateCustomizationEventDTO -> customizationInputPort.update(
                        customization = record.toDomain()
                    )

                    is PatchCustomizationEventDTO -> customizationInputPort.patch(
                        customization = record.toDomain()
                    )


                    is CreateOptionEventDTO -> optionInputPort.create(
                        option = record.toDomain()
                    )

                    is DeleteOptionEventDTO -> optionInputPort.delete(
                        storeId = record.storeId.toString().toUUID(),
                        optionId = record.optionId.toString().toUUID()
                    )

                    is UpdateOptionEventDTO -> optionInputPort.update(
                        option = record.toDomain()
                    )

                    is PatchOptionEventDTO -> optionInputPort.patch(
                        option = record.toDomain()
                    )

                    else -> throw UnsupportedOperationException("${record::class.qualifiedName} request not mapped yet")
                }
            }.onFailure { logger.error("Error while processing record", it) }
        }

        acknowledgment.acknowledge()
    }
}