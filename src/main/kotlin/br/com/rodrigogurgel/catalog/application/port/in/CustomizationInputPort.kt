package br.com.rodrigogurgel.catalog.application.port.`in`

import br.com.rodrigogurgel.catalog.domain.Customization
import com.github.michaelbull.result.Result
import java.util.UUID

interface CustomizationInputPort {
    fun create(customization: Customization): Result<Unit, Throwable>
    fun update(customization: Customization): Result<Unit, Throwable>
    fun patch(customization: Customization): Result<Unit, Throwable>
    fun delete(storeId: UUID, customizationId: UUID): Result<Unit, Throwable>
    fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Customization>, Throwable>
}