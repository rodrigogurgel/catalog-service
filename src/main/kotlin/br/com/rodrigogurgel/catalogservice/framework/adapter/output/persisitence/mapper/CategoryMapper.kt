package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CategoryMapper : RowMapper<Category> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Category =
        Category(
            id = rs.id(),
            name = rs.name(),
            description = rs.description(),
            status = rs.status()
        )
}
