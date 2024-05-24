package br.com.rodrigogurgel.catalog.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.CategoryDataStoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalog.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalog.domain.Category
import java.util.UUID
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest

@Repository
class CategoryDynamoDBDatastore(
    private val dynamoDbTable: DynamoDbTable<CategoryDataStoreDTO>,
) : CategoryDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(category_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override fun create(category: Category) {
        val request = PutItemEnhancedRequest
            .builder(CategoryDataStoreDTO::class.java)
            .item(category.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.putItem(request)
    }

    override fun update(category: Category) {
        val request = UpdateItemEnhancedRequest
            .builder(CategoryDataStoreDTO::class.java)
            .ignoreNulls(false)
            .item(category.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.updateItem(request)
    }

    override fun delete(storeId: UUID, categoryId: UUID) {
        val request = DeleteItemEnhancedRequest
            .builder()
            .key(
                Key
                    .builder()
                    .partitionValue(storeId.toString())
                    .sortValue(categoryId.toString())
                    .build()
            )
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.deleteItem(request)
    }

    override fun patch(category: Category) {
        val request = UpdateItemEnhancedRequest
            .builder(CategoryDataStoreDTO::class.java)
            .ignoreNulls(false)
            .item(category.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.updateItem(request)
    }

}