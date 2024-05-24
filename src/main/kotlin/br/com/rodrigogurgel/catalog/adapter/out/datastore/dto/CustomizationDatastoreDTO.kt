package br.com.rodrigogurgel.catalog.adapter.out.datastore.dto

import java.util.UUID
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
data class CustomizationDatastoreDTO(
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("customization_id")
    var customizationId: UUID? = null,

    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("store_id")
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["ReferenceIndex"])
    var storeId: UUID? = null,

    @get:DynamoDbAttribute("name")
    var name: String? = null,

    @get:DynamoDbAttribute("description")
    var description: String? = null,

    @get:DynamoDbAttribute("min_permitted")
    var minPermitted: Int? = null,

    @get:DynamoDbAttribute("max_permitted")
    var maxPermitted: Int? = null,

    @get:DynamoDbAttribute("status")
    var status: StatusDatastoreDTO? = null,

    @get:DynamoDbAttribute("index")
    var index: Int? = null,

    @get:DynamoDbAttribute("reference")
    @get:DynamoDbSecondarySortKey(indexNames = ["ReferenceIndex"])
    var reference: String? = null,
)
