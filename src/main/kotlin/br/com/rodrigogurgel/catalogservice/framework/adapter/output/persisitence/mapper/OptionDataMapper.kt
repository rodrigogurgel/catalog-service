package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OptionData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.CustomizationDataMapper.Companion.toData
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class OptionDataMapper : RowMapper<OptionData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): OptionData =
        TODO("Not yet implemented")

    companion object {
        fun Option.toData(storeId: UUID, offerId: UUID, customizationId: UUID): OptionData {
            return OptionData(
                optionId = id.value,
                offerId = offerId,
                storeId = storeId,
                productId = product.id.value,
                customizationId = customizationId,
                minPermitted = quantity.minPermitted,
                maxPermitted = quantity.maxPermitted,
                price = price.normalizedValue(),
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
    }
}