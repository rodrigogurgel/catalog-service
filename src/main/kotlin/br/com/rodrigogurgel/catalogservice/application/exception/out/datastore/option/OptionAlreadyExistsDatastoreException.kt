package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option

import java.util.UUID

data class OptionAlreadyExistsDatastoreException(val optionId: UUID) : RuntimeException(
    "Option $optionId already exists"
)
