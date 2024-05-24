package br.com.rodrigogurgel.catalog.adapter.out.datastore.config

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.CategoryDataStoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.OptionDatastoreDTO
import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.ProductDatastoreDTO
import java.net.URI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


@Configuration
class DynamoDBConfig {

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient
            .builder()
            .endpointOverride(URI.create("http://localhost:8000"))
            .region(Region.SA_EAST_1)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                AwsBasicCredentials.create("DUMMYID","DUMMYEXAMPLEKEY")))

            .build()
    }

    @Bean
    fun enhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
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
    fun categoryDynamoDbTable(
        enhancedClient: DynamoDbEnhancedClient,
        schema: TableSchema<CategoryDataStoreDTO>,
    ) : DynamoDbTable<CategoryDataStoreDTO> = enhancedClient.table("category", schema)

    @Bean
    fun productDynamoDbTable(
        enhancedClient: DynamoDbEnhancedClient,
        schema: TableSchema<ProductDatastoreDTO>,
    ): DynamoDbTable<ProductDatastoreDTO> = enhancedClient.table("product", schema)

    @Bean
    fun itemDynamoDbTable(
        enhancedClient: DynamoDbEnhancedClient,
        schema: TableSchema<ItemDatastoreDTO>,
    ): DynamoDbTable<ItemDatastoreDTO> = enhancedClient.table("item", schema)

    @Bean
    fun customizationDynamoDbTable(
        enhancedClient: DynamoDbEnhancedClient,
        schema: TableSchema<CustomizationDatastoreDTO>,
    ): DynamoDbTable<CustomizationDatastoreDTO> = enhancedClient.table("customization", schema)

    @Bean
    fun optionDynamoDbTable(
        enhancedClient: DynamoDbEnhancedClient,
        schema: TableSchema<OptionDatastoreDTO>,
    ): DynamoDbTable<OptionDatastoreDTO> = enhancedClient.table("option", schema)

}