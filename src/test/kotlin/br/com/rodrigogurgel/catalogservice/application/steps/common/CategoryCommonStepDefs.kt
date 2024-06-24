package br.com.rodrigogurgel.catalogservice.application.steps.common

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Given
import io.mockk.every

class CategoryCommonStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
) {
    @Given("the following category information's:")
    fun theFollowingCategoryInformationS(category: Category) {
        categoryContext.category = category
    }

    @Given("a id {string} with no category associated")
    fun aIdWithNoCategoryAssociated(categoryId: String) {
        every {
            categoryContext.categoryDatastoreOutputPort.findById(
                storeContext.store.id,
                Id(categoryId)
            )
        } returns null
    }

    @Given("the following category exists:")
    fun theFollowingCategoryExists(category: Category) {
        categoryContext.category = category

        every {
            categoryContext.categoryDatastoreOutputPort.findById(
                storeContext.store.id,
                category.id
            )
        } returns category

        every {
            categoryContext.categoryDatastoreOutputPort.exists(
                storeContext.store.id,
                match { category.id == it }
            )
        } returns true

        every {
            categoryContext.categoryDatastoreOutputPort.update(
                storeContext.store.id,
                match { category.id == it.id }
            )
        } returns Unit
    }
}
