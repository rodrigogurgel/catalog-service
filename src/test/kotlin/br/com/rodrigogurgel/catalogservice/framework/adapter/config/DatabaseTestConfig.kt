package br.com.rodrigogurgel.catalogservice.framework.adapter.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile
import javax.sql.DataSource

@Profile("test")
@TestConfiguration
class DatabaseTestConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    fun jdbcDatabaseContainer(): JdbcDatabaseContainer<*> {
        return PostgreSQLContainer("postgres:16-alpine")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource(
                    "postgres/init.sql"
                ),
                "/docker-entrypoint-initdb.d/"
            )
            .waitingFor(Wait.forListeningPort())
    }

    @Bean
    fun dataSource(jdbcDatabaseContainer: JdbcDatabaseContainer<*>): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = jdbcDatabaseContainer.jdbcUrl
        hikariConfig.username = jdbcDatabaseContainer.username
        hikariConfig.password = jdbcDatabaseContainer.password
        return HikariDataSource(hikariConfig)
    }

    @Bean
    fun namedParameterJdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate =
        NamedParameterJdbcTemplate(dataSource)
}
