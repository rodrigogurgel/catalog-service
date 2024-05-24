package br.com.rodrigogurgel.catalog.application.port.`in`

import br.com.rodrigogurgel.catalog.domain.Category
import com.github.michaelbull.result.Result
import java.util.UUID

interface CategoryInputPort {
    fun create(category: Category): Result<Unit, Throwable>
    fun update(category: Category): Result<Unit, Throwable>
    fun delete(storeId: UUID, categoryId: UUID): Result<Unit, Throwable>
    fun patch(category: Category): Result<Unit, Throwable>
}