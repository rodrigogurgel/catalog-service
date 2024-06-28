package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class OfferNotFoundException private constructor(override val message: String) : IllegalStateException(message) {
    constructor(storeId: Id, offerId: Id) : this("Offer with id ${offerId.value} and Store id $storeId not found")
}
