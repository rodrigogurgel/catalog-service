package br.com.rodrigogurgel.catalogservice.domain

enum class TransactionType {
    CREATE_CATEGORY,
    DELETE_CATEGORY,
    UPDATE_CATEGORY,

    CREATE_ITEM,
    DELETE_ITEM,
    UPDATE_ITEM,

    CREATE_PRODUCT,
    DELETE_PRODUCT,
    UPDATE_PRODUCT,

    CREATE_CUSTOMIZATION,
    DELETE_CUSTOMIZATION,
    UPDATE_CUSTOMIZATION,

    CREATE_OPTION,
    DELETE_OPTION,
    UPDATE_OPTION,
}
