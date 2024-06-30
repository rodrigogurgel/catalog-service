package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ProductMapper : RowMapper<Product> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Product =
        Product(
            id = rs.id(),
            name = rs.name(),
            description = rs.description(),
            image = rs.image()
        )
}
