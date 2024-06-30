package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import java.sql.ResultSet
import java.util.UUID

fun ResultSet.id(): Id {
    return Id(UUID.fromString(getString("id")))
}

fun ResultSet.name(): Name {
    return Name(getString("name"))
}

fun ResultSet.description(): Description? {
    return getString("description")?.let { Description(it) }
}

fun ResultSet.image(): Image? {
    return getString("image_path")?.let { Image(it) }
}
