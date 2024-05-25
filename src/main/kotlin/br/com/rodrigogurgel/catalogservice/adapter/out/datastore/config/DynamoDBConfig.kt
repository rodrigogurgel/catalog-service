package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.config

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CategoryDataStoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.OptionDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ProductDatastoreDTO
import java.net.URI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


@Configuration
class DynamoDBConfig {

    @Bean
    fun dynamoDbAsyncClient(): DynamoDbAsyncClient {
        return DynamoDbAsyncClient
            .builder()
            .endpointOverride(URI.create("http://localhost:8000"))
            .region(Region.SA_EAST_1)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("DUMMYID","DUMMYEXAMPLEKEY")))

            .build()
    }

    @Bean
    fun enhancedAsyncClient(dynamoDbAsyncClient: DynamoDbAsyncClient): DynamoDbEnhancedAsyncClient = DynamoDbEnhancedAsyncClient.builder()
        .dynamoDbClient(dynamoDbAsyncClient)
        .build()

    @Bean
    fun categoryTableSchema() : TableSchema<CategoryDataStoreDTO> = TableSchema.fromBean(CategoryDataStoreDTO::class.java)

    @Bean
    fun productTableSchema() : TableSchema<ProductDatastoreDTO> = TableSchema.fromBean(ProductDatastoreDTO::class.java)

    @Bean
    fun itemSchema() : TableSchema<ItemDatastoreDTO> = TableSchema.fromBean(ItemDatastoreDTO::class.java)

    @Bean
    fun customizationTableSchema() : TableSchema<CustomizationDatastoreDTO> = TableSchema.fromBean(CustomizationDatastoreDTO::class.java)

    @Bean
    fun optionTableSchema() : TableSchema<OptionDatastoreDTO> = TableSchema.fromBean(OptionDatastoreDTO::class.java)

    @Bean
    fun categoryDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
        schema: TableSchema<CategoryDataStoreDTO>,
    ) : DynamoDbAsyncTable<CategoryDataStoreDTO> = enhancedAsyncClient.table("category", schema)
//        .apply { createTable() }

    @Bean
    fun productDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
        schema: TableSchema<ProductDatastoreDTO>,
    ): DynamoDbAsyncTable<ProductDatastoreDTO> = enhancedAsyncClient.table("product", schema)
//        .apply { createTable() }

    @Bean
    fun itemDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
        schema: TableSchema<ItemDatastoreDTO>,
    ): DynamoDbAsyncTable<ItemDatastoreDTO> = enhancedAsyncClient.table("item", schema)
//        .apply { createTable() }

    @Bean
    fun customizationDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
        schema: TableSchema<CustomizationDatastoreDTO>,
    ): DynamoDbAsyncTable<CustomizationDatastoreDTO> = enhancedAsyncClient.table("customization", schema)
//        .apply { createTable() }

    @Bean
    fun optionDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
        schema: TableSchema<OptionDatastoreDTO>,
    ): DynamoDbAsyncTable<OptionDatastoreDTO> = enhancedAsyncClient.table("option", schema)
//        .apply { createTable() }

}