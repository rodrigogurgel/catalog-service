package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OptionData

interface OptionRepository {
    fun createBatch(options: List<OptionData>)
}