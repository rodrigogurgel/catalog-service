package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.time.OffsetDateTime
import java.util.UUID

@DynamoDbBean
data class IdempotencyDatastoreDTO(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("idempotency_id")
    var idempotencyId: UUID? = null,

    @get:DynamoDbAttribute("correlation_id")
    var correlationId: UUID? = null,

    @get:DynamoDbAttribute("store_id")
    var storeId: UUID? = null,

    @get:DynamoDbAttribute("status")
    var status: IdempotencyStatusDatastoreDTO? = null,

    @get:DynamoDbAttribute("created_at")
    var createdAt: OffsetDateTime? = null,
)
