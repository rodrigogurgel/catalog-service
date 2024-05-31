package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnoreNulls
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@DynamoDbBean
data class OptionDatastoreDTO(

    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("option_id")
    var optionId: UUID? = null,

    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("store_id")
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["ReferenceIndex", "ProductIdIndex"])
    var storeId: UUID? = null,

    @get:DynamoDbAttribute("customization_id")
    var customizationId: UUID? = null,

    @get:DynamoDbAttribute("product_id")
    @get:DynamoDbSecondarySortKey(indexNames = ["ProductIdIndex"])
    var productId: UUID? = null,

    @get:DynamoDbAttribute("price")
    var price: BigDecimal? = null,

    @get:DynamoDbAttribute("status")
    var status: StatusDatastoreDTO? = null,

    @get:DynamoDbAttribute("index")
    var index: Int? = null,

    @get:DynamoDbAttribute("min_permitted")
    val minPermitted: Int? = null,

    @get:DynamoDbAttribute("max_permitted")
    val maxPermitted: Int? = null,

    @get:DynamoDbAttribute("reference")
    @get:DynamoDbSecondarySortKey(indexNames = ["ReferenceIndex"])
    @get:DynamoDbIgnoreNulls
    var reference: String? = null,

    @get:DynamoDbAttribute("created_at")
    var createdAt: Instant? = null,

    @get:DynamoDbAttribute("updated_at")
    var updatedAt: Instant? = null,
)
