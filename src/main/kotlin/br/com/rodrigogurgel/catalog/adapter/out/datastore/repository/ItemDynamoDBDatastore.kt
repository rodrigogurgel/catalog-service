package br.com.rodrigogurgel.catalog.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalog.application.common.getPkAndSkOrNull
import br.com.rodrigogurgel.catalog.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalog.domain.Item
import java.util.UUID
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest

@Repository
class ItemDynamoDBDatastore(
    private val dynamoDbTable: DynamoDbTable<ItemDatastoreDTO>,
) : ItemDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override fun create(item: Item) {
        val request = PutItemEnhancedRequest
            .builder(ItemDatastoreDTO::class.java)
            .item(item.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.putItem(request)
    }

    override fun update(item: Item) {

    }

    override fun delete(storeId: UUID, itemId: UUID) {

    }

    override fun patch(item: Item) {

    }

    override fun findByReference(reference: String): Item {
        val (pk, sk) = reference.getPkAndSkOrNull() ?: throw RuntimeException()
        val key = buildKey(pk, sk)

        return dynamoDbTable.getItem(key).toDomain()
    }

    override fun find(storeId: UUID, itemId: UUID): Item {
        val request = GetItemEnhancedRequest.builder().key(
            Key
            .builder()
            .partitionValue(storeId.toString())
            .sortValue(itemId.toString())
            .build()
        ).build()

        return dynamoDbTable.getItem(request).toDomain()
//        val key =
//
//        val query = SingleKeyItemConditional(key, "=")
//
//        val index = dynamoDbTable.index("StoreItemIndex")
//        val item = index.query(query).flatMap { it.items() }.first()
//
//        return item.toDomain()
    }

    private fun buildKey(item: Item): Key {
        return Key
            .builder()
            .partitionValue("STORE#${item.storeId.toString()}")
            .sortValue(item.reference)
            .build()
    }

    private fun buildKey(pk: String, sk: String): Key {
        return Key
            .builder()
            .partitionValue(pk)
            .sortValue(sk)
            .build()
    }
}