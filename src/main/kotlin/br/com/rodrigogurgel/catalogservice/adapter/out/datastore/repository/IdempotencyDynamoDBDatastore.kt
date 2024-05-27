package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.IdempotencyDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.idempotency.IdempotencyAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.idempotency.IdempotencyNotFoundDatastoreException
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.IdempotencyOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Idempotency
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
class IdempotencyDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<IdempotencyDatastoreDTO>,
) : IdempotencyOutputPort {
    companion object {
        private val NOT_EXISTS_EXPRESSION =
            Expression.builder().expression("attribute_not_exists(idempotency_id)").build()
        private val EXISTS_EXPRESSION =
            Expression.builder().expression("attribute_exists(idempotency_id)").build()
    }

    override suspend fun create(idempotency: Idempotency): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = PutItemEnhancedRequest
            .builder(IdempotencyDatastoreDTO::class.java)
            .item(idempotency.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> IdempotencyAlreadyExistsException(idempotency.idempotencyId!!)
            else -> error
        }
    }

    override suspend fun patch(idempotency: Idempotency): Result<Unit, Throwable> = runSuspendCatching<Unit> {
        val request = UpdateItemEnhancedRequest
            .builder(IdempotencyDatastoreDTO::class.java)
            .ignoreNulls(true)
            .item(idempotency.toDatastoreDTO())
            .conditionExpression(EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.updateItem(request).await()
    }.mapError { error ->
        when (error) {
            is ConditionalCheckFailedException -> IdempotencyNotFoundDatastoreException(idempotency.idempotencyId!!)

            else -> error
        }
    }

    override suspend fun search(idempotencyId: UUID): Result<Idempotency?, Throwable> = runSuspendCatching {
        val request = GetItemEnhancedRequest.builder().key(
            Key
                .builder()
                .partitionValue(idempotencyId.toString())
                .build()
        ).build()

        dynamoDbAsyncTable.getItem(request)?.await()?.toDomain()
    }
}
