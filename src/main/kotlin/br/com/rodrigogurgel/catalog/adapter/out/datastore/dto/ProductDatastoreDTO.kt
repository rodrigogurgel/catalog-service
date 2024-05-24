package br.com.rodrigogurgel.catalog.adapter.out.datastore.dto

import java.util.UUID
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
data class ProductDatastoreDTO(
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("product_id")
    var productId: UUID? = null,

    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("store_id")
    var storeId: UUID? = null,

    @get:DynamoDbAttribute("name")
    var name: String? = null,

    @get:DynamoDbAttribute("description")
    var description: String? = null,

    @get:DynamoDbAttribute("image")
    var image: String? = null
)
