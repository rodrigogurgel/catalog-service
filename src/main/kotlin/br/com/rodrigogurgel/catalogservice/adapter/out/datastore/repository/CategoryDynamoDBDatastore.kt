package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CategoryDataStoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.category.CategoryAlreadyExistsDatastoreException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.category.CategoryNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Category
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import java.util.UUID
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException

@Repository
class CategoryDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<CategoryDataStoreDTO>,
) : CategoryDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(category_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override suspend fun create(category: Category): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest
            .builder(CategoryDataStoreDTO::class.java)
            .item(category.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CategoryAlreadyExistsDatastoreException(category.categoryId!!)
            else -> error
        }
    }

    override suspend fun update(category: Category): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = UpdateItemEnhancedRequest
            .builder(CategoryDataStoreDTO::class.java)
            .ignoreNulls(false)
            .item(category.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CategoryNotFoundDatastoreException(
                category.storeId!!,
                category.categoryId!!
            )

            else -> error
        }
    }

    override suspend fun delete(storeId: UUID, categoryId: UUID): Result<Unit, Throwable> = runSuspendCatching<Unit> {
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

        dynamoDbAsyncTable.deleteItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CategoryNotFoundDatastoreException(
                storeId,
                categoryId
            )

            else -> error
        }
    }

    override suspend fun patch(category: Category): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = UpdateItemEnhancedRequest
            .builder(CategoryDataStoreDTO::class.java)
            .ignoreNulls(true)
            .item(category.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> throw CategoryNotFoundDatastoreException(
                category.storeId!!,
                category.categoryId!!
            )

            else -> error
        }
    }
}
