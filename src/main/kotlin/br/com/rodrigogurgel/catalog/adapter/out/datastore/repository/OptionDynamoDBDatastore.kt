package br.com.rodrigogurgel.catalog.adapter.out.datastore.repository

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.OptionDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper.toDomain
import br.com.rodrigogurgel.catalog.application.common.getPkAndSkOrNull
import br.com.rodrigogurgel.catalog.application.port.out.datastore.OptionDatastoreOutputPort
import br.com.rodrigogurgel.catalog.domain.Option
import java.util.UUID
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch

@Repository
class OptionDynamoDBDatastore(
    private val dynamoDbTable: DynamoDbTable<OptionDatastoreDTO>,
    private val enhancedClient: DynamoDbEnhancedClient,
) : OptionDatastoreOutputPort {

    companion object {
        private val NOT_EXISTS_EXPRESSION = Expression.builder().expression("attribute_not_exists(item_id)").build()
        private val EXISTS_EXPRESSION = Expression.builder().expression("attribute_exists(category_id)").build()
    }

    override fun create(option: Option) {
        val request = PutItemEnhancedRequest
            .builder(OptionDatastoreDTO::class.java)
            .item(option.toDatastoreDTO())
            .conditionExpression(NOT_EXISTS_EXPRESSION)
            .build()

        dynamoDbTable.putItem(request)
    }

    override fun update(option: Option) {

    }

    override fun delete(storeId: UUID, optionId: UUID) {

    }

    override fun patch(option: Option) {

    }

    override fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Option> {
        val keys = getKeysFromReference(storeId, reference).ifEmpty { return emptyList() }

        val readBatch = keys.map { key ->
            ReadBatch
                .builder(OptionDatastoreDTO::class.java)
                .addGetItem(key)
                .mappedTableResource(dynamoDbTable)
                .build()
        }

        val batchGetItem = BatchGetItemEnhancedRequest
            .builder()
            .readBatches(readBatch)
            .build()

        return enhancedClient.batchGetItem(batchGetItem).resultsForTable(dynamoDbTable)
            .map { option -> option.toDomain() }
    }

    private fun getKeysFromReference(storeId: UUID, reference: String): Set<Key> {
        val condition = QueryConditional.sortBeginsWith(
            Key.builder()
                .partitionValue(storeId.toString())
                .sortValue(reference)
                .build()
        )

        val request = QueryEnhancedRequest.builder()
            .queryConditional(condition)
            .build()

        return dynamoDbTable.index("ReferenceIndex")
            .query(request)
            .flatMap { it.items() }
            .map { option ->
                Key.builder()
                    .partitionValue(option.storeId.toString())
                    .sortValue(option.optionId.toString())
                    .build()
            }.toSet()
    }
}