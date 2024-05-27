package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization

import java.util.UUID

data class CustomizationNotFoundDatastoreException(val storeId: UUID, val customizationId: UUID) :
    RuntimeException("Customization $customizationId not found in store $storeId")
