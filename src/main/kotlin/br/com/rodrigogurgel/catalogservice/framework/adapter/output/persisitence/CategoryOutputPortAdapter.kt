package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.CategoryDataMapper.Companion.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.CategoryDataMapper.Companion.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.CategoryRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class CategoryOutputPortAdapter(
    private val categoryRepository: CategoryRepository,
) : CategoryOutputPort {
    override fun create(storeId: Id, category: Category) {
        categoryRepository.create(category.toData(storeId.value))
    }

    override fun findById(storeId: Id, categoryId: Id): Category? {
        return categoryRepository.findById(storeId.value, categoryId.value)?.toEntity()
    }

    override fun exists(categoryId: Id): Boolean {
        return categoryRepository.exists(categoryId.value)
    }

    override fun exists(storeId: Id, categoryId: Id): Boolean {
        return categoryRepository.exists(storeId.value, categoryId.value)
    }

    override fun delete(storeId: Id, categoryId: Id) {
        categoryRepository.delete(storeId.value, categoryId.value)
    }

    override fun update(storeId: Id, category: Category) {
        categoryRepository.update(category.toData(storeId.value))
    }

    override fun getCategories(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Category> {
        return categoryRepository.getCategories(storeId.value, limit, offset, beginsWith)
            .map { categoryData -> categoryData.toEntity() }
    }

    override fun countCategories(storeId: Id, beginsWith: String?): Int {
        return categoryRepository.countCategories(storeId.value, beginsWith)
    }
}
