package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.utils

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.sql.ResultSet
import java.util.UUID

fun ResultSet.getUUID(columnLabel: String): UUID? {
    return getString(columnLabel)?.let { UUID.fromString(it) }
}

fun ResultSet.getStatus(columnLabel: String): Status? {
    return getString(columnLabel)?.let { Status.valueOf(it) }
}

// fun ResultSet.getId(columnLabel: String): Id? {
//    return UUID.fromString(getString(columnLabel))?.let { Id(it) }
// }
//
// fun ResultSet.getName(columnLabel: String): Name? {
//    return getString(columnLabel)?.let { Name(it) }
// }
//
// fun ResultSet.getDescription(columnLabel: String): Description? {
//    return getString(columnLabel)?.let { Description(it) }
// }
//
// fun ResultSet.getImage(columnLabel: String): Image? {
//    return getString(columnLabel)?.let { Image(it) }
// }
//

//
// fun ResultSet.getPrice(columnLabel: String): Price? {
//    return getString(columnLabel)?.let { Price(it.toBigDecimal()) }
// }
