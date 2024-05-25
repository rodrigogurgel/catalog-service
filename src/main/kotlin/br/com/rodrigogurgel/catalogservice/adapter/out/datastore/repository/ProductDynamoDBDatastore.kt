package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ProductDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Product
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
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch

@Repository
class ProductDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<ProductDatastoreDTO>,
    private val enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
) : ProductDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override suspend fun create(product: Product) {
        val request = PutItemEnhancedRequest
            .builder(ProductDatastoreDTO::class.java)
            .item(product.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request)
    }

    override suspend fun update(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(storeId: UUID, productId: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun patch(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun find(storeId: UUID, productIds: Set<UUID>): List<Product> {
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
        return withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher).toList().blockingGet()
        }
            .map { product -> product.toDomain() }
    }
}