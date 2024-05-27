package br.com.rodrigogurgel.catalogservice.application.port.out.events

import br.com.rodrigogurgel.catalogservice.domain.Category
import com.github.michaelbull.result.Result
import java.util.UUID

interface CategoryEventOutputPort {
    fun created(transactionId: UUID, category: Category): Result<Unit, Throwable>
    fun updated(transactionId: UUID, category: Category): Result<Unit, Throwable>
    fun deleted(transactionId: UUID, category: Category): Result<Unit, Throwable>
    fun patched(transactionId: UUID, category: Category): Result<Unit, Throwable>
}
