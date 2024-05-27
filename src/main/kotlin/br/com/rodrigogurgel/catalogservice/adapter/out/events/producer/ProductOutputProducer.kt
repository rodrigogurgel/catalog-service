package br.com.rodrigogurgel.catalogservice.adapter.out.events.producer

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toProductCreatedDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toProductDeletedDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toProductPatchedDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toProductUpdatedDTO
import br.com.rodrigogurgel.catalogservice.application.port.out.events.ProductEventOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductOutputProducer(
    private val genericOutputProducer: GenericOutputProducer
) : ProductEventOutputPort {
    override fun created(transactionId: UUID, product: Product): Result<Unit, Throwable> =
        product.toProductCreatedDTO().andThen { genericOutputProducer.send(transactionId, it) }

    override fun updated(transactionId: UUID, product: Product): Result<Unit, Throwable> =
        product.toProductUpdatedDTO().andThen { genericOutputProducer.send(transactionId, it) }

    override fun deleted(transactionId: UUID, product: Product): Result<Unit, Throwable> =
        product.toProductDeletedDTO().andThen { genericOutputProducer.send(transactionId, it) }

    override fun patched(transactionId: UUID, product: Product): Result<Unit, Throwable> =
        product.toProductPatchedDTO().andThen { genericOutputProducer.send(transactionId, it) }
}