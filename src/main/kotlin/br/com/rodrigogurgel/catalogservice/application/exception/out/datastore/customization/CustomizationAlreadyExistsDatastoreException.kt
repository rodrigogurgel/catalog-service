package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.customization

import java.util.UUID

data class CustomizationAlreadyExistsDatastoreException(val customizationId: UUID) :
    RuntimeException("Customization $customizationId already exists")
