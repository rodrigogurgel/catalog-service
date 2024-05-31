package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.option

import java.util.UUID

data class OptionNotFoundDatastoreException(val storeId: UUID, val optionId: UUID) : RuntimeException(
    "Option $optionId not found in store $storeId"
)
