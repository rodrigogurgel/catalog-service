package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.common.getPkAndSkOrNull
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Item
import java.util.UUID
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest

@Repository
class ItemDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<ItemDatastoreDTO>,
) : ItemDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override suspend fun create(item: Item) {
        val request = PutItemEnhancedRequest
            .builder(ItemDatastoreDTO::class.java)
            .item(item.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request)
    }

    override suspend fun update(item: Item) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(storeId: UUID, itemId: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun patch(item: Item) {
        TODO("Not yet implemented")
    }

    override suspend fun find(storeId: UUID, itemId: UUID): Item {
        val request = GetItemEnhancedRequest.builder().key(
            Key
            .builder()
            .partitionValue(storeId.toString())
            .sortValue(itemId.toString())
            .build()
        ).build()

        return dynamoDbAsyncTable.getItem(request).await().toDomain()
    }
}