package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.OfferData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.ProductData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getStatus
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getUUID
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class OfferDataMapper : RowMapper<OfferData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): OfferData {
        return OfferData(
            offerId = rs.getUUID("offer_id")!!,
            productId = rs.getUUID("product_id")!!,
            categoryId = rs.getUUID("category_id")!!,
            storeId = rs.getUUID("store_id")!!,
            name = rs.getString("name"),
            price = rs.getBigDecimal("price"),
            status = rs.getStatus("status"),
            customizations = emptyList(),
        )
    }
}

fun OfferData.toEntity(productById: Map<UUID, ProductData>): Offer = Offer(
    id = Id(offerId),
    name = Name(name),
    product = productById[productId]!!.toEntity(),
    price = Price(price),
    status = status,
    customizations = customizations.map { customizationData ->
        customizationData.toEntity(
            productById
        )
    }.toMutableList(),
)

fun Offer.toData(storeId: UUID, categoryId: UUID): OfferData = OfferData(
    offerId = id.value,
    productId = product.id.value,
    categoryId = categoryId,
    storeId = storeId,
    name = name.value,
    price = price.value,
    status = status,
    customizations = customizations.map { customization ->
        customization.toData(
            storeId = storeId,
            offerId = id.value,
            optionId = null,
        )
    },
)
