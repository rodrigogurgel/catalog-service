package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization.BatchGetCustomizationDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization.CustomizationAlreadyExistsDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization.CustomizationNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CustomizationDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Customization
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onFailure
import io.reactivex.rxjava3.core.Flowable
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException

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

    override suspend fun create(customization: Customization): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .item(customization.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CustomizationAlreadyExistsDatastoreException(
                customization.customizationId!!
            )

            else -> error
        }
    }

    override suspend fun update(customization: Customization): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .ignoreNulls(false)
            .item(customization.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CustomizationNotFoundDatastoreException(
                customization.storeId!!,
                customization.customizationId!!
            )

            else -> error
        }
    }

    override suspend fun delete(storeId: UUID, customizationId: UUID): Result<Unit, Throwable> =
        runSuspendCatching<Unit> {
            val request = DeleteItemEnhancedRequest
                .builder()
                .key(
                    Key
                        .builder()
                        .partitionValue(storeId.toString())
                        .sortValue(customizationId.toString())
                        .build()
                )
                .conditionExpression(EXISTS_EXPRESSION)
                .build()

            dynamoDbAsyncTable.deleteItem(request).await()
        }.mapError { error ->
            when (error) {
                is ConditionalCheckFailedException -> throw CustomizationNotFoundDatastoreException(
                    storeId,
                    customizationId
                )

                else -> error
            }
        }

    override suspend fun patch(customization: Customization): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(CustomizationDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(customization.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CustomizationNotFoundDatastoreException(
                customization.storeId!!,
                customization.customizationId!!
            )

            else -> error
        }
    }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Customization>, Throwable> = getKeysFromReference(storeId, reference)
        .andThen { findByKeys(it) }
        .onFailure { throw it }

    private suspend fun getKeysFromReference(storeId: UUID, reference: String): Result<Set<Key>, Throwable> =
        runSuspendCatching {
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

            Flowable.fromPublisher(publisher).toList().blockingGet()
                .flatMap { it.items() }
                .map { customization ->
                    Key.builder()
                        .partitionValue(customization.storeId.toString())
                        .sortValue(customization.customizationId.toString())
                        .build()
                }.toSet()
        }.onFailure { throw it }

    private suspend fun findByKeys(keys: Set<Key>): Result<List<Customization>, Throwable> = runSuspendCatching {
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

        withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher).map { customization -> customization.toDomain() }.toList().blockingGet()
        }
    }.onFailure { throw BatchGetCustomizationDatastoreException(keys) }
}
