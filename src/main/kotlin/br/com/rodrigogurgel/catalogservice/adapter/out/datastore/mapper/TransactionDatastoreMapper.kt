package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.TransactionDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.TransactionStatusDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.TransactionTypeDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionStatus
import br.com.rodrigogurgel.catalogservice.domain.TransactionType

fun Transaction<*>.toDatastoreDTO(): TransactionDatastoreDTO {
    return TransactionDatastoreDTO(
        transactionId = transactionId,
        correlationId = correlationId,
        storeId = storeId,
        type = type?.toDatastoreDTO(),
        status = status?.toDatastoreDTO(),
        message = message,
        createdAt = createdAt,
        createdBy = createdBy,
        createdFrom = createdFrom,
        updatedAt = updatedAt,
    )
}

fun <T> TransactionDatastoreDTO.toDomain(): Transaction<T> {
    return Transaction(
        transactionId = transactionId,
        correlationId = correlationId,
        storeId = storeId,
        type = type?.toDomain(),
        data = null,
        status = status?.toDomain(),
        message = message,
        createdAt = createdAt,
        createdBy = createdBy,
        createdFrom = createdFrom,
        updatedAt = updatedAt,
    )
}

fun TransactionStatus.toDatastoreDTO(): TransactionStatusDatastoreDTO {
    return when (this) {
        TransactionStatus.CREATED -> TransactionStatusDatastoreDTO.CREATED
        TransactionStatus.SUCCESS -> TransactionStatusDatastoreDTO.SUCCESS
        TransactionStatus.FAILURE -> TransactionStatusDatastoreDTO.FAILURE
        TransactionStatus.DISCARD -> TransactionStatusDatastoreDTO.DISCARD
    }
}

fun TransactionStatusDatastoreDTO.toDomain(): TransactionStatus {
    return when (this) {
        TransactionStatusDatastoreDTO.CREATED -> TransactionStatus.CREATED
        TransactionStatusDatastoreDTO.SUCCESS -> TransactionStatus.SUCCESS
        TransactionStatusDatastoreDTO.FAILURE -> TransactionStatus.FAILURE
        TransactionStatusDatastoreDTO.DISCARD -> TransactionStatus.DISCARD
    }
}

fun TransactionType.toDatastoreDTO(): TransactionTypeDatastoreDTO {
    return when (this) {
        TransactionType.CREATE_CATEGORY -> TransactionTypeDatastoreDTO.CREATE_CATEGORY
        TransactionType.DELETE_CATEGORY -> TransactionTypeDatastoreDTO.DELETE_CATEGORY
        TransactionType.UPDATE_CATEGORY -> TransactionTypeDatastoreDTO.UPDATE_CATEGORY
        TransactionType.CREATE_ITEM -> TransactionTypeDatastoreDTO.CREATE_ITEM
        TransactionType.DELETE_ITEM -> TransactionTypeDatastoreDTO.DELETE_ITEM
        TransactionType.UPDATE_ITEM -> TransactionTypeDatastoreDTO.UPDATE_ITEM
        TransactionType.CREATE_PRODUCT -> TransactionTypeDatastoreDTO.CREATE_PRODUCT
        TransactionType.DELETE_PRODUCT -> TransactionTypeDatastoreDTO.DELETE_PRODUCT
        TransactionType.UPDATE_PRODUCT -> TransactionTypeDatastoreDTO.UPDATE_PRODUCT
        TransactionType.CREATE_CUSTOMIZATION -> TransactionTypeDatastoreDTO.CREATE_CUSTOMIZATION
        TransactionType.DELETE_CUSTOMIZATION -> TransactionTypeDatastoreDTO.DELETE_CUSTOMIZATION
        TransactionType.UPDATE_CUSTOMIZATION -> TransactionTypeDatastoreDTO.UPDATE_CUSTOMIZATION
        TransactionType.CREATE_OPTION -> TransactionTypeDatastoreDTO.CREATE_OPTION
        TransactionType.DELETE_OPTION -> TransactionTypeDatastoreDTO.DELETE_OPTION
        TransactionType.UPDATE_OPTION -> TransactionTypeDatastoreDTO.UPDATE_OPTION
    }
}

fun TransactionTypeDatastoreDTO.toDomain(): TransactionType {
    return when (this) {
        TransactionTypeDatastoreDTO.CREATE_CATEGORY -> TransactionType.CREATE_CATEGORY
        TransactionTypeDatastoreDTO.DELETE_CATEGORY -> TransactionType.DELETE_CATEGORY
        TransactionTypeDatastoreDTO.UPDATE_CATEGORY -> TransactionType.UPDATE_CATEGORY
        TransactionTypeDatastoreDTO.CREATE_ITEM -> TransactionType.CREATE_ITEM
        TransactionTypeDatastoreDTO.DELETE_ITEM -> TransactionType.DELETE_ITEM
        TransactionTypeDatastoreDTO.UPDATE_ITEM -> TransactionType.UPDATE_ITEM
        TransactionTypeDatastoreDTO.CREATE_PRODUCT -> TransactionType.CREATE_PRODUCT
        TransactionTypeDatastoreDTO.DELETE_PRODUCT -> TransactionType.DELETE_PRODUCT
        TransactionTypeDatastoreDTO.UPDATE_PRODUCT -> TransactionType.UPDATE_PRODUCT
        TransactionTypeDatastoreDTO.CREATE_CUSTOMIZATION -> TransactionType.CREATE_CUSTOMIZATION
        TransactionTypeDatastoreDTO.DELETE_CUSTOMIZATION -> TransactionType.DELETE_CUSTOMIZATION
        TransactionTypeDatastoreDTO.UPDATE_CUSTOMIZATION -> TransactionType.UPDATE_CUSTOMIZATION
        TransactionTypeDatastoreDTO.CREATE_OPTION -> TransactionType.CREATE_OPTION
        TransactionTypeDatastoreDTO.DELETE_OPTION -> TransactionType.DELETE_OPTION
        TransactionTypeDatastoreDTO.UPDATE_OPTION -> TransactionType.UPDATE_OPTION
    }
}
