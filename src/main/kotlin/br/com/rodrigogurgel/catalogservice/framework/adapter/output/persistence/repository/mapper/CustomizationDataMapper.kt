package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.CustomizationData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.ProductData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getStatus
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.utils.getUUID
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class CustomizationDataMapper : RowMapper<CustomizationData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CustomizationData =
        CustomizationData(
            customizationId = rs.getUUID("customization_id")!!,
            offerId = rs.getUUID("offer_id")!!,
            storeId = rs.getUUID("store_id")!!,
            optionId = rs.getUUID("option_id"),
            name = rs.getString("name"),
            description = rs.getString("description"),
            minPermitted = rs.getInt("min_permitted"),
            maxPermitted = rs.getInt("max_permitted"),
            status = rs.getStatus("status"),
        )
}

fun CustomizationData.toEntity(productById: Map<UUID, ProductData>): Customization {
    return Customization(
        id = Id(customizationId),
        name = Name(name),
        description = description?.let { Description(description) },
        quantity = Quantity(minPermitted, maxPermitted),
        status = status,
        options = options.map { optionData -> optionData.toEntity(productById) }.toMutableList()
    )
}

fun Customization.toData(storeId: UUID, offerId: UUID, optionId: UUID?): CustomizationData {
    return CustomizationData(
        customizationId = id.value,
        offerId = offerId,
        storeId = storeId,
        optionId = optionId,
        name = name.value,
        description = description?.value,
        minPermitted = quantity.minPermitted,
        maxPermitted = quantity.maxPermitted,
        status = status,
        options = options.map { option ->
            option.toData(
                storeId = storeId,
                offerId = offerId,
                customizationId = id.value
            )
        },
    )
}
