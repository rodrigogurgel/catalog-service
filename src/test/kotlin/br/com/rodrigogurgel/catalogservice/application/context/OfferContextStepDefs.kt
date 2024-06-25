package br.com.rodrigogurgel.catalogservice.application.context

import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import io.mockk.mockk

class OfferContextStepDefs {
    val offerDatastoreOutputPort: OfferDatastoreOutputPort = mockk<OfferDatastoreOutputPort>()
    lateinit var offer: Offer
}
