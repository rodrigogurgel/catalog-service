package br.com.rodrigogurgel.catalog.application.common

import java.util.UUID

fun String.getPkAndSkOrNull(): Pair<String, String>? {
    val result = "^(.*?)#CATEGORY#".toRegex().find(this)
    val pk = result?.groups?.get(1)?.value ?: return null
    val sk = this.removePrefix("$pk#")
    return pk to sk
}

fun String.toUUID(): UUID = UUID.fromString(this)
