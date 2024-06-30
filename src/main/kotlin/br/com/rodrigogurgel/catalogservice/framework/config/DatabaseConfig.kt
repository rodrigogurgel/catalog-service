package br.com.rodrigogurgel.catalogservice.framework.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
@Profile("!test")
class DatabaseConfig(
    @Value("\${spring.datasource.url}") private val url: String,
    @Value("\${spring.datasource.username}") private val username: String,
    @Value("\${spring.datasource.password}") private val password: String,
    @Value("\${spring.datasource.hikari.maximum-pool-size:6}") private val maximumPoolSize: Int,
    @Value("\${spring.datasource.hikari.leak-detection-threshold:30000}") private val leakDetectionThreshold: Long,
    @Value("\${spring.datasource.connection-timeout:5000}") private val connectionTimeout: Long,
) {
    @Bean
    fun hikariConfig(): HikariConfig {
        val hikariConfig = HikariConfig()

        hikariConfig.jdbcUrl = url
        hikariConfig.username = username
        hikariConfig.password = password
        hikariConfig.maximumPoolSize = maximumPoolSize
        hikariConfig.leakDetectionThreshold = leakDetectionThreshold
        hikariConfig.connectionTimeout = connectionTimeout

        return hikariConfig
    }

    @Bean
    fun dataSource(hikariConfig: HikariConfig): DataSource =
        HikariDataSource(hikariConfig)

    @Bean
    fun namedParameterJdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate =
        NamedParameterJdbcTemplate(dataSource)
}
