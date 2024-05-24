package br.com.rodrigogurgel.catalog.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.ProductDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalog.application.common.getPkAndSkOrNull
import br.com.rodrigogurgel.catalog.application.port.out.datastore.CustomizationDatastoreOutputPort
import br.com.rodrigogurgel.catalog.domain.Customization
import java.util.UUID
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest

@Repository
class CustomizationDynamoDBDatastore(
    private val dynamoDbTable: DynamoDbTable<CustomizationDatastoreDTO>,
    private val enhancedClient: DynamoDbEnhancedClient,
) : CustomizationDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION =
            Expression.builder().expression("attribute_not_exists(customization_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override fun create(customization: Customization) {
        val request = PutItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .item(customization.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.putItem(request)
    }

    override fun update(customization: Customization) {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .ignoreNulls(false)
            .item(customization.toDatastoreDTO())
            .build()

        dynamoDbTable.updateItem(updateItemRequest)
    }

    override fun delete(storeId: UUID, customizationId: UUID) {
        dynamoDbTable.deleteItem(
            Key
                .builder()
                .partitionValue(storeId.toString())
                .sortValue(customizationId.toString())
                .build()
        )
    }

    override fun patch(customization: Customization) {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(customization.toDatastoreDTO())
            .build()

        dynamoDbTable.updateItem(updateItemRequest)
    }

    override fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Customization> {
        val keys = getKeysFromReference(storeId, reference).ifEmpty { return emptyList() }

        val readBatch = keys.map { key ->
            ReadBatch
                .builder(CustomizationDatastoreDTO::class.java)
                .addGetItem(key)
                .mappedTableResource(dynamoDbTable)
                .build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest
            .builder()
            .readBatches(readBatch)
            .build()

        return enhancedClient.batchGetItem(batchGetItem).resultsForTable(dynamoDbTable)
            .map { customization -> customization.toDomain() }
    }

    private fun getKeysFromReference(storeId: UUID, reference: String): Set<Key> {
        val condition = QueryConditional.sortBeginsWith(
            Key.builder()
                .partitionValue(storeId.toString())
                .sortValue(reference)
                .build()
        )

        val request = QueryEnhancedRequest.builder()
            .queryConditional(condition)
            .build()

        return dynamoDbTable.index("ReferenceIndex")
            .query(request)
            .flatMap { it.items() }
            .map { customization ->
                Key.builder()
                    .partitionValue(customization.storeId.toString())
                    .sortValue(customization.customizationId.toString())
                    .build()
            }.toSet()
    }
}