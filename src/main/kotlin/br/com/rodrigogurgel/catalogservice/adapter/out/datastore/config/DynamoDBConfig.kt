package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.config

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.config.extensions.createTableWithIndices
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
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.ProjectionType

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
                    AwsBasicCredentials.create("DUMMYID", "DUMMYEXAMPLEKEY")
                )
            )
            .build()
    }

    @Bean
    fun enhancedAsyncClient(dynamoDbAsyncClient: DynamoDbAsyncClient): DynamoDbEnhancedAsyncClient =
        DynamoDbEnhancedAsyncClient.builder()
            .dynamoDbClient(dynamoDbAsyncClient)
            .build()

    @Bean
    fun categoryDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
    ): DynamoDbAsyncTable<CategoryDataStoreDTO> =
        enhancedAsyncClient.table("category", TableSchema.fromBean(CategoryDataStoreDTO::class.java))
            .apply { createTableWithIndices(ProjectionType.KEYS_ONLY, ProjectionType.KEYS_ONLY) }

    @Bean
    fun productDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
    ): DynamoDbAsyncTable<ProductDatastoreDTO> =
        enhancedAsyncClient.table("product", TableSchema.fromBean(ProductDatastoreDTO::class.java))
            .apply { createTableWithIndices(ProjectionType.KEYS_ONLY, ProjectionType.KEYS_ONLY) }

    @Bean
    fun itemDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
    ): DynamoDbAsyncTable<ItemDatastoreDTO> =
        enhancedAsyncClient.table("item", TableSchema.fromBean(ItemDatastoreDTO::class.java))
            .apply { createTableWithIndices(ProjectionType.KEYS_ONLY, ProjectionType.KEYS_ONLY) }

    @Bean
    fun customizationDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
    ): DynamoDbAsyncTable<CustomizationDatastoreDTO> =
        enhancedAsyncClient.table("customization", TableSchema.fromBean(CustomizationDatastoreDTO::class.java))
            .apply { createTableWithIndices(ProjectionType.KEYS_ONLY, ProjectionType.KEYS_ONLY) }

    @Bean
    fun optionDynamoDbAsyncTable(
        enhancedAsyncClient: DynamoDbEnhancedAsyncClient,
    ): DynamoDbAsyncTable<OptionDatastoreDTO> =
        enhancedAsyncClient.table("option", TableSchema.fromBean(OptionDatastoreDTO::class.java))
            .apply { createTableWithIndices(ProjectionType.KEYS_ONLY, ProjectionType.KEYS_ONLY) }

}
