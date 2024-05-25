package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto

import java.util.UUID
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
data class CategoryDataStoreDTO(
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("category_id")
    var categoryId: UUID? = null,

    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("store_id")
    var storeId: UUID? = null,

    @get:DynamoDbAttribute("name")
    var name: String? = null,

    @get:DynamoDbAttribute("status")
    var status: StatusDatastoreDTO? = null,

    @get:DynamoDbAttribute("index")
    var index: Int? = null,
)