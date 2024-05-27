package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ProductDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.product.BatchGetProductDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.product.ProductAlreadyExistsDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.product.ProductNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
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
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException

@Repository
class ProductDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<ProductDatastoreDTO>,
    private val enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
) : ProductDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(product_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(product_id)").build()
    }

    override suspend fun create(product: Product): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest
            .builder(ProductDatastoreDTO::class.java)
            .item(product.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ProductAlreadyExistsDatastoreException(
                product.productId!!
            )

            else -> error
        }
    }

    override suspend fun update(product: Product): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(ProductDatastoreDTO::class.java)
            .ignoreNulls(false)
            .item(product.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ProductNotFoundDatastoreException(
                product.storeId!!,
                product.productId!!
            )

            else -> error
        }
    }

    override suspend fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = DeleteItemEnhancedRequest
            .builder()
            .key(
                Key
                    .builder()
                    .partitionValue(storeId.toString())
                    .sortValue(productId.toString())
                    .build()
            )
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.deleteItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ProductNotFoundDatastoreException(
                storeId,
                productId
            )

            else -> error
        }
    }

    override suspend fun patch(product: Product): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val updateItemRequest = UpdateItemEnhancedRequest
            .builder(ProductDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(product.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(updateItemRequest).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw ProductNotFoundDatastoreException(
                product.storeId!!,
                product.productId!!
            )

            else -> error
        }
    }

    override suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable> =
        runSuspendCatching {
            val readBatch: Collection<ReadBatch> = productIds.map { id ->
                ReadBatch.builder(ProductDatastoreDTO::class.java).addGetItem(
                    Key
                        .builder()
                        .partitionValue(storeId.toString())
                        .sortValue(id.toString())
                        .build()
                ).mappedTableResource(dynamoDbAsyncTable).build()
            }

            val batchGetItem = BatchGetItemEnhancedRequest.builder().readBatches(readBatch).build()

            val publisher = enhancedAsyncClient.batchGetItem(batchGetItem).resultsForTable(dynamoDbAsyncTable)

            withContext(Dispatchers.IO) {
                Flowable.fromPublisher(publisher).toList().blockingGet()
            }
                .map { product -> product.toDomain() }
        }.onFailure {
            throw BatchGetProductDatastoreException(
                productIds.map { id ->
                    Key
                        .builder()
                        .partitionValue(storeId.toString())
                        .sortValue(id.toString())
                        .build()
                }.toSet()
            )
        }
}
