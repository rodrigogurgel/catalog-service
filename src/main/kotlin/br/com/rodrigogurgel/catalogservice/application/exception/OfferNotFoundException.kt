package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class OfferNotFoundException private constructor(override val message: String) : IllegalStateException(message) {
    constructor(
        storeId: Id,
        offerId: Id,
    ) : this("Offer with the id ${offerId.value} and Store with the id $storeId not found")
}
