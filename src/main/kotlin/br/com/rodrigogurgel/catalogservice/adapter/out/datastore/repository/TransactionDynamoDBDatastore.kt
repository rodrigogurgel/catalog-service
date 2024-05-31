package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.TransactionDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.transaction.TransactionAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.transaction.TransactionNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.TransactionOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import java.util.UUID

@Service
class TransactionDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<TransactionDatastoreDTO>,
) : TransactionOutputPort {
    companion object {
        private val NOT_EXISTS_EXPRESSION =
            Expression.builder().expression("attribute_not_exists(transaction_id)").build()
        private val EXISTS_EXPRESSION =
            Expression.builder().expression("attribute_exists(transaction_id)").build()
    }

    override suspend fun <T> create(transaction: Transaction<T>): Result<Transaction<T>, Throwable> =
        runSuspendCatching {
            val request = PutItemEnhancedRequest
                .builder(TransactionDatastoreDTO::class.java)
                .item(transaction.toDatastoreDTO())
                .conditionExpression(NOT_EXISTS_EXPRESSION)
                .build()

            dynamoDbAsyncTable.putItem(request).await()

            transaction
        }.mapError { error ->
            when (error) {
                is ConditionalCheckFailedException -> TransactionAlreadyExistsException(transaction.transactionId!!)
                else -> error
            }
        }

    override suspend fun <T> update(transaction: Transaction<T>): Result<Transaction<T>, Throwable> =
        runSuspendCatching {
            val request = UpdateItemEnhancedRequest
                .builder(TransactionDatastoreDTO::class.java)
                .item(transaction.toDatastoreDTO())
                .conditionExpression(EXISTS_EXPRESSION)
                .build()

            dynamoDbAsyncTable.updateItem(request).await()
            transaction
        }.mapError { error ->
            when (error) {
                is ConditionalCheckFailedException -> TransactionNotFoundDatastoreException(transaction.transactionId!!)

                else -> error
            }
        }

    override suspend fun <T> search(transactionId: UUID): Result<Transaction<T>?, Throwable> = runSuspendCatching {
        val request = GetItemEnhancedRequest.builder().key(
            Key
                .builder()
                .partitionValue(transactionId.toString())
                .build()
        ).build()

        dynamoDbAsyncTable.getItem(request)?.await()?.toDomain()
    }
}
