package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.OptionDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.OptionDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Option
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
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch

@Repository
class OptionDynamoDBDatastore(
    private val dynamoDbAsyncTable: DynamoDbAsyncTable<OptionDatastoreDTO>,
    private val enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
) : OptionDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override suspend fun create(option: Option) {
        val request = PutItemEnhancedRequest
            .builder(OptionDatastoreDTO::class.java)
            .item(option.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbAsyncTable.putItem(request)
    }

    override suspend fun update(option: Option) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(storeId: UUID, optionId: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun patch(option: Option) {
        TODO("Not yet implemented")
    }

    override suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Option> {
        val keys = getKeysFromReference(storeId, reference).ifEmpty { return emptyList() }

        val readBatch = keys.map { key ->
            ReadBatch
                .builder(OptionDatastoreDTO::class.java)
                .addGetItem(key)
                .mappedTableResource(dynamoDbAsyncTable)
                .build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest
            .builder()
            .readBatches(readBatch)
            .build()

        val publisher = enhancedAsyncClient.batchGetItem(batchGetItem)
            .resultsForTable(dynamoDbAsyncTable)

        return withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher)
                .map { option -> option.toDomain() }.toList().blockingGet()
        }

    }

    private suspend fun getKeysFromReference(storeId: UUID, reference: String): Set<Key> {
        val condition = QueryConditional.sortBeginsWith(
            Key.builder()
                .partitionValue(storeId.toString())
                .sortValue(reference)
                .build()
        )

        val request = QueryEnhancedRequest.builder()
            .queryConditional(condition)
            .build()

        val publisher = dynamoDbAsyncTable.index("ReferenceIndex")
            .query(request)

        return withContext(Dispatchers.IO) {
            Flowable.fromPublisher(publisher).toList().blockingGet()
        }
            .flatMap { it.items() }
            .map { option ->
                Key.builder()
                    .partitionValue(option.storeId.toString())
                    .sortValue(option.optionId.toString())
                    .build()
            }.toSet()
    }
}