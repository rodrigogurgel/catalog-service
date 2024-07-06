package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.ProductData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getUUID
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class ProductDataMapper : RowMapper<ProductData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): ProductData =
        ProductData(
            productId = rs.getUUID("product_id")!!,
            storeId = rs.getUUID("store_id")!!,
            name = rs.getString("name"),
            description = rs.getString("description"),
            imagePath = rs.getString("image_path"),
        )
}

fun Product.toData(storeId: UUID): ProductData {
    return ProductData(
        productId = id.value,
        storeId = storeId,
        name = name.value,
        description = description?.value,
        imagePath = image?.path
    )
}

fun ProductData.toEntity(): Product {
    return Product(
        id = Id(productId),
        name = Name(name),
        description = description?.let { Description(it) },
        image = imagePath?.let { Image(path = it) },
    )
}
