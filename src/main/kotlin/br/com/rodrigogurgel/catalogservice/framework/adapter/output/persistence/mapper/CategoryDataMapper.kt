package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.CategoryData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.utils.getStatus
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.utils.getUUID
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class CategoryDataMapper : RowMapper<CategoryData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CategoryData =
        CategoryData(
            categoryId = rs.getUUID("category_id")!!,
            storeId = rs.getUUID("store_id")!!,
            name = rs.getString("name"),
            description = rs.getString("description"),
            status = rs.getStatus("status"),
        )
}

fun Category.toData(storeId: UUID): CategoryData = CategoryData(
    categoryId = id.value,
    storeId = storeId,
    name = name.value,
    description = description?.value,
    status = status,
)

fun CategoryData.toEntity(): Category = Category(
    id = Id(categoryId),
    name = Name(name),
    description = description?.let { Description(description) },
    status = status,
)
