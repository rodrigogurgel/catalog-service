package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CustomizationDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Customization
import io.reactivex.rxjava3.core.Flowable
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
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
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<CustomizationDatastoreDTO>,
    private val enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
) : CustomizationDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION =
            Expression.builder().expression("attribute_not_exists(customization_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override suspend fun create(customization: Customization) {
        val request = PutItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .item(customization.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request)
    }

    override suspend fun update(customization: Customization) {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .ignoreNulls(false)
            .item(customization.toDatastoreDTO())
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest)
    }

    override suspend fun delete(storeId: UUID, customizationId: UUID) {
        dynamoDbAsyncTable.deleteItem(
            Key
                .builder()
                .partitionValue(storeId.toString())
                .sortValue(customizationId.toString())
                .build()
        )
    }

    override suspend fun patch(customization: Customization) {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(customization.toDatastoreDTO())
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest)
    }

    override suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Customization> {
        val keys = getKeysFromReference(storeId, reference).ifEmpty { return emptyList() }

        val readBatch = keys.map { key ->
            ReadBatch
                .builder(CustomizationDatastoreDTO::class.java)
                .addGetItem(key)
                .mappedTableResource(dynamoDbAsyncTable)
                .build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest
            .builder()
            .readBatches(readBatch)
            .build()

        val publisher = enhancedAsyncClient.batchGetItem(batchGetItem).resultsForTable(dynamoDbAsyncTable)
        return withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher).map { customization -> customization.toDomain() }.toList().blockingGet()
        }

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

        val publisher = dynamoDbAsyncTable.index("ReferenceIndex").query(request)

        return Flowable.fromPublisher(publisher).toList().blockingGet()
            .flatMap { it.items() }
            .map { customization ->
                Key.builder()
                    .partitionValue(customization.storeId.toString())
                    .sortValue(customization.customizationId.toString())
                    .build()
            }.toSet()
    }
}