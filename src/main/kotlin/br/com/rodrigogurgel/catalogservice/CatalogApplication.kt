package br.com.rodrigogurgel.catalogservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalogApplication

fun main(args: Array<String>) {
    runApplication<CatalogApplication>(args = args)
}
