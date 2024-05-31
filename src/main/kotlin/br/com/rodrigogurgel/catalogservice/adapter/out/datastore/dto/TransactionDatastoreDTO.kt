package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.time.Instant
import java.util.UUID

@DynamoDbBean
data class TransactionDatastoreDTO(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("transaction_id")
    var transactionId: UUID? = null,

    @get:DynamoDbAttribute("correlation_id")
    var correlationId: UUID? = null,

    @get:DynamoDbAttribute("store_id")
    var storeId: UUID? = null,

    @get:DynamoDbAttribute("type")
    var type: TransactionTypeDatastoreDTO? = null,

    @get:DynamoDbAttribute("status")
    var status: TransactionStatusDatastoreDTO? = null,

    @get:DynamoDbAttribute("message")
    var message: String? = null,

    @get:DynamoDbAttribute("created_by")
    var createdBy: String? = null,

    @get:DynamoDbAttribute("created_from")
    var createdFrom: String? = null,

    @get:DynamoDbAttribute("created_at")
    var createdAt: Instant? = null,

    @get:DynamoDbAttribute("updated_at")
    var updatedAt: Instant? = null,
)
