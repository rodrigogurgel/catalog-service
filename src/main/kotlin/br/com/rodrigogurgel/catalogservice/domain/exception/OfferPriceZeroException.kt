package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class OfferPriceZeroException(offerId: Id) :
    IllegalStateException("Offer $offerId has price equals to zero")
