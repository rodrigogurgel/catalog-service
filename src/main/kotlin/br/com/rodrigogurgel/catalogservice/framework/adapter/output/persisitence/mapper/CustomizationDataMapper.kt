package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.CustomizationData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.CustomizationDataMapper.Companion.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.OptionDataMapper.Companion.toData
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class CustomizationDataMapper : RowMapper<CustomizationData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CustomizationData =
        TODO("Not yet implemented")

    companion object {
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
    }
}