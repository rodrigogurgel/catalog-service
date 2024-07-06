package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class OfferAlreadyExistsException(offerId: Id) :
    IllegalArgumentException("Offer with the $offerId already exists")
