package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.item.ItemAlreadyExistsDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.item.ItemNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option.BatchGetOptionDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import io.reactivex.rxjava3.core.Flowable
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
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import java.util.UUID

@Repository
class ItemDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<ItemDatastoreDTO>,
    private val enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
) : ItemDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(item_id)").build()
    }

    override suspend fun create(item: Item): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest.builder(ItemDatastoreDTO::class.java).item(item.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION).build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> ItemAlreadyExistsDatastoreException(item.itemId!!)
            else -> error
        }
    }

    override suspend fun update(item: Item): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest =
            UpdateItemEnhancedRequest.builder(ItemDatastoreDTO::class.java).item(item.toDatastoreDTO())
                .conditionExpression(EXISTS_EXPRESSION).build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> ItemNotFoundDatastoreException(
                item.storeId!!, item.itemId!!
            )

            else -> error
        }
    }

    override suspend fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = DeleteItemEnhancedRequest.builder().key(
            Key.builder().partitionValue(storeId.toString()).sortValue(itemId.toString()).build()
        ).conditionExpression(EXISTS_EXPRESSION).build()

        dynamoDbAsyncTable.deleteItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> ItemNotFoundDatastoreException(
                storeId, itemId
            )

            else -> error
        }
    }

    override suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> = runSuspendCatching {
        val request = GetItemEnhancedRequest.builder().key(
            Key.builder().partitionValue(storeId.toString()).sortValue(itemId.toString()).build()
        ).build()

        dynamoDbAsyncTable.getItem(request)?.await()?.toDomain() ?: throw ItemNotFoundDatastoreException(
            storeId, itemId
        )
    }

    override suspend fun searchByProductId(storeId: UUID, productId: UUID): Result<List<Item>, Throwable> =
        getKeysByProductId(storeId, productId).andThen { findByKeys(it) }

    override suspend fun searchByCategoryId(storeId: UUID, categoryId: UUID): Result<List<Item>, Throwable> =
        getKeysByCategoryId(storeId, categoryId).andThen { findByKeys(it) }


    private suspend fun findByKeys(keys: Set<Key>): Result<List<Item>, Throwable> = runSuspendCatching {
        keys.ifEmpty { return@runSuspendCatching emptyList() }

        val readBatch = keys.map { key ->
            ReadBatch.builder(ItemDatastoreDTO::class.java).addGetItem(key).mappedTableResource(dynamoDbAsyncTable)
                .build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest.builder().readBatches(readBatch).build()

        val publisher = enhancedAsyncClient.batchGetItem(batchGetItem).resultsForTable(dynamoDbAsyncTable)

        withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher).map { option -> option.toDomain() }.toList().blockingGet()
        }
    }.mapError { BatchGetOptionDatastoreException(keys, it) }

    private suspend fun getKeysByProductId(storeId: UUID, productId: UUID): Result<Set<Key>, Throwable> =
        runSuspendCatching {
            val condition = QueryConditional.sortBeginsWith(
                Key.builder().partitionValue(storeId.toString()).sortValue(productId.toString()).build()
            )

            val request = QueryEnhancedRequest.builder().queryConditional(condition).build()

            val publisher = dynamoDbAsyncTable.index("ProductIdIndex").query(request)

            withContext(Dispatchers.IO) {
                Flowable.fromPublisher(publisher).toList().blockingGet()
            }.flatMap { it.items() }.map { option ->
                Key.builder().partitionValue(option.storeId.toString()).sortValue(option.itemId.toString()).build()
            }.toSet()
        }

    private suspend fun getKeysByCategoryId(storeId: UUID, categoryId: UUID): Result<Set<Key>, Throwable> =
        runSuspendCatching {
            val condition = QueryConditional.sortBeginsWith(
                Key.builder().partitionValue(storeId.toString()).sortValue(categoryId.toString()).build()
            )

            val request = QueryEnhancedRequest.builder().queryConditional(condition).build()

            val publisher = dynamoDbAsyncTable.index("CategoryIdIndex").query(request)

            withContext(Dispatchers.IO) {
                Flowable.fromPublisher(publisher).toList().blockingGet()
            }.flatMap { it.items() }.map { option ->
                Key.builder().partitionValue(option.storeId.toString()).sortValue(option.itemId.toString()).build()
            }.toSet()
        }
}
