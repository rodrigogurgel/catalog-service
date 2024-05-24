package br.com.rodrigogurgel.catalog.application.port.`in`

import br.com.rodrigogurgel.catalog.domain.Option
import com.github.michaelbull.result.Result
import java.util.UUID

interface OptionInputPort {
    fun create(option: Option): Result<Unit, Throwable>
    fun update(option: Option): Result<Unit, Throwable>
    fun delete(storeId: UUID, optionId: UUID): Result<Unit, Throwable>
    fun patch(option: Option): Result<Unit, Throwable>
    fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Option>, Throwable>
}