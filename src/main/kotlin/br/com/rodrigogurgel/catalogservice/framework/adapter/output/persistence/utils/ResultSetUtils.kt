package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.utils

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.sql.ResultSet
import java.util.UUID

fun ResultSet.getUUID(columnLabel: String): UUID? {
    return getString(columnLabel)?.let { UUID.fromString(it) }
}

fun ResultSet.getStatus(columnLabel: String): Status {
    return Status.valueOf(getString(columnLabel))
}
