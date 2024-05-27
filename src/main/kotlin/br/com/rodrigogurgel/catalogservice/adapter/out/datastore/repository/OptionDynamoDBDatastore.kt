package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.OptionDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option.BatchGetOptionDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option.OptionAlreadyExistsDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option.OptionNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.OptionDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Option
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
import org.slf4j.LoggerFactory
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
class OptionDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<OptionDatastoreDTO>,
    private val enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
) : OptionDatastoreOutputPort {

    private val logger = LoggerFactory.getLogger(OptionDynamoDBDatastore::class.java)

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(option_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(option_id)").build()
    }

    override suspend fun create(option: Option): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest
            .builder(OptionDatastoreDTO::class.java)
            .item(option.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw OptionAlreadyExistsDatastoreException(
                option.optionId!!
            )

            else -> throw error
        }
    }

    override suspend fun update(option: Option): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(OptionDatastoreDTO::class.java)
            .ignoreNulls(false)
            .item(option.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw OptionNotFoundDatastoreException(
                option.storeId!!,
                option.optionId!!
            )

            else -> throw error
        }
    }

    override suspend fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = DeleteItemEnhancedRequest
            .builder()
            .key(
                Key
                    .builder()
                    .partitionValue(storeId.toString())
                    .sortValue(optionId.toString())
                    .build()
            )
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.deleteItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw OptionNotFoundDatastoreException(
                storeId,
                optionId
            )

            else -> throw error
        }
    }

    override suspend fun patch(option: Option): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(OptionDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(option.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw OptionNotFoundDatastoreException(
                option.storeId!!,
                option.optionId!!
            )

            else -> throw error
        }
    }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Option>, Throwable> = getKeysFromReference(storeId, reference)
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

            val publisher = dynamoDbAsyncTable.index("ReferenceIndex")
                .query(request)

            withContext(Dispatchers.IO) {
                Flowable.fromPublisher(publisher).toList().blockingGet()
            }
                .flatMap { it.items() }
                .map { option ->
                    Key.builder()
                        .partitionValue(option.storeId.toString())
                        .sortValue(option.optionId.toString())
                        .build()
                }.toSet()
        }.onFailure { throw it }

    private suspend fun findByKeys(keys: Set<Key>): Result<List<Option>, Throwable> = runSuspendCatching {
        val readBatch = keys.map { key ->
            ReadBatch
                .builder(OptionDatastoreDTO::class.java)
                .addGetItem(key)
                .mappedTableResource(dynamoDbAsyncTable)
                .build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest
            .builder()
            .readBatches(readBatch)
            .build()

        val publisher = enhancedAsyncClient.batchGetItem(batchGetItem)
            .resultsForTable(dynamoDbAsyncTable)

        withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher)
                .map { option -> option.toDomain() }.toList().blockingGet()
        }
    }.onFailure { throw BatchGetOptionDatastoreException(keys) }
}
