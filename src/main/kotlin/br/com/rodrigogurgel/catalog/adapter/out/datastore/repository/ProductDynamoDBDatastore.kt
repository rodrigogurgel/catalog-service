package br.com.rodrigogurgel.catalog.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.ProductDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalog.application.port.out.datastore.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalog.domain.Product
import java.util.UUID
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch

@Repository
class ProductDynamoDBDatastore(
    private val dynamoDbTable: DynamoDbTable<ProductDatastoreDTO>,
    private val enhancedClient: DynamoDbEnhancedClient,
) : ProductDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override fun create(product: Product) {
        val request = PutItemEnhancedRequest
            .builder(ProductDatastoreDTO::class.java)
            .item(product.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.putItem(request)
    }

    override fun update(product: Product) {

    }

    override fun delete(storeId: UUID, productId: UUID) {

    }

    override fun patch(product: Product) {

    }

    override fun find(storeId: UUID, productIds: Set<UUID>): List<Product> {
        val readBatch: Collection<ReadBatch> = productIds.map { id ->
            ReadBatch.builder(ProductDatastoreDTO::class.java).addGetItem(
                Key
                    .builder()
                    .partitionValue(storeId.toString())
                    .sortValue(id.toString())
                    .build()
            ).mappedTableResource(dynamoDbTable).build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest.builder().readBatches(readBatch).build()

        return enhancedClient.batchGetItem(batchGetItem).resultsForTable(dynamoDbTable)
            .map { product -> product.toDomain() }
    }
}