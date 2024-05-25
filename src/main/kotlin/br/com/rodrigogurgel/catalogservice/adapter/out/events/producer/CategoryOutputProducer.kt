package br.com.rodrigogurgel.catalogservice.adapter.out.events.producer

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toCategoryCreatedDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toCategoryDeletedDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toCategoryPatchedDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.toCategoryUpdatedDTO
import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CategoryEventOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class CategoryOutputProducer(
    private val genericOutputProducer: GenericOutputProducer,
) : CategoryEventOutputPort {

    override fun created(transactionId: UUID, category: Category): Result<Unit, Throwable> =
        category.toCategoryCreatedDTO()
            .andThen { genericOutputProducer.send(transactionId, it) }

    override fun updated(transactionId: UUID, category: Category): Result<Unit, Throwable> =
        category.toCategoryUpdatedDTO()
            .andThen { genericOutputProducer.send(transactionId, it) }

    override fun deleted(transactionId: UUID, category: Category): Result<Unit, Throwable> =
        category.toCategoryDeletedDTO()
            .andThen { genericOutputProducer.send(transactionId, it) }

    override fun patched(transactionId: UUID, category: Category): Result<Unit, Throwable> =
        category.toCategoryPatchedDTO()
            .andThen { genericOutputProducer.send(transactionId, it) }
}