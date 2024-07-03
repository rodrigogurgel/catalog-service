package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.CustomizationData

interface CustomizationRepository {
    fun createBatch(customizations: List<CustomizationData>)
}