package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.item.ItemAlreadyExistsDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.item.ItemNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onFailure
import java.util.UUID
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException

@Repository
class ItemDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<ItemDatastoreDTO>,
) : ItemDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(item_id)").build()
    }

    override suspend fun create(item: Item): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest
            .builder(ItemDatastoreDTO::class.java)
            .item(item.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ItemAlreadyExistsDatastoreException(item.itemId!!)
            else -> throw error
        }
    }

    override suspend fun update(item: Item): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(ItemDatastoreDTO::class.java)
            .ignoreNulls(false)
            .item(item.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ItemNotFoundDatastoreException(
                item.storeId!!,
                item.itemId!!
            )

            else -> throw error
        }
    }

    override suspend fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = DeleteItemEnhancedRequest
            .builder()
            .key(
                Key
                    .builder()
                    .partitionValue(storeId.toString())
                    .sortValue(itemId.toString())
                    .build()
            )
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.deleteItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ItemNotFoundDatastoreException(
                storeId,
                itemId
            )

            else -> throw error
        }
    }

    override suspend fun patch(item: Item): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(ItemDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(item.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ItemNotFoundDatastoreException(item.storeId!!, item.itemId!!)
            else -> throw error
        }
    }

    override suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> = runSuspendCatching {
        val request = GetItemEnhancedRequest.builder().key(
            Key
                .builder()
                .partitionValue(storeId.toString())
                .sortValue(itemId.toString())
                .build()
        ).build()

        dynamoDbAsyncTable.getItem(request)?.await()?.toDomain() ?: throw ItemNotFoundDatastoreException(
            storeId,
            itemId
        )
    }.onFailure { throw it }
}
