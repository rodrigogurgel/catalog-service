package br.com.rodrigogurgel.catalogservice.domain.entity

data class Page<T>(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val data: List<T>,
)
