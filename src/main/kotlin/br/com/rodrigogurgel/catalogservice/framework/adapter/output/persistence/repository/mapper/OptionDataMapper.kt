package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.OptionData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.ProductData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getStatus
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getUUID
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class OptionDataMapper : RowMapper<OptionData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): OptionData =
        OptionData(
            optionId = rs.getUUID("option_id")!!,
            offerId = rs.getUUID("offer_id")!!,
            storeId = rs.getUUID("store_id")!!,
            productId = rs.getUUID("product_id")!!,
            customizationId = rs.getUUID("customization_id")!!,
            minPermitted = rs.getInt("min_permitted"),
            maxPermitted = rs.getInt("max_permitted"),
            price = rs.getBigDecimal("price"),
            status = rs.getStatus("status"),
        )
}

fun OptionData.toEntity(productById: Map<UUID, ProductData>): Option {
    return Option(
        id = Id(optionId),
        product = productById[productId]!!.toEntity(),
        price = Price(price),
        quantity = Quantity(minPermitted, maxPermitted),
        status = status,
        customizations = customizations.map { customizationData -> customizationData.toEntity(productById) }
            .toMutableList()
    )
}

fun Option.toData(storeId: UUID, offerId: UUID, customizationId: UUID): OptionData {
    return OptionData(
        optionId = id.value,
        offerId = offerId,
        storeId = storeId,
        productId = product.id.value,
        customizationId = customizationId,
        minPermitted = quantity.minPermitted,
        maxPermitted = quantity.maxPermitted,
        price = price.value,
        status = status,
        customizations = customizations.map { customization ->
            customization.toData(
                storeId = storeId,
                offerId = offerId,
                optionId = id.value
            )
        }
    )
}
