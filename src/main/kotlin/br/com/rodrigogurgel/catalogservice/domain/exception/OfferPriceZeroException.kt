package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class OfferPriceZeroException(offerId: Id) :
    IllegalArgumentException("Offer $offerId has price equals to zero")
