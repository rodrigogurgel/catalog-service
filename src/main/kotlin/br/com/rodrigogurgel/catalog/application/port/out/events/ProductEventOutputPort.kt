package br.com.rodrigogurgel.catalog.application.port.out.events

import br.com.rodrigogurgel.catalog.domain.Product
import com.github.michaelbull.result.Result
import java.util.UUID

interface ProductEventOutputPort {
    fun created(transactionId: UUID, product: Product): Result<Unit, Throwable>
    fun updated(transactionId: UUID, product: Product): Result<Unit, Throwable>
    fun deleted(transactionId: UUID, product: Product): Result<Unit, Throwable>
    fun patched(transactionId: UUID, product: Product): Result<Unit, Throwable>
}