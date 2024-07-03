package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OfferData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.CustomizationDataMapper.Companion.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.OfferDataMapper.Companion.toData
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

class OfferDataMapper : RowMapper<OfferData> {
    override fun mapRow(rs: ResultSet, rowNum: Int): OfferData =
        TODO("Not yet implemented")

    companion object {
        fun Offer.toData(storeId: UUID, categoryId: UUID): OfferData = OfferData(
            offerId = id.value,
            productId = product.id.value,
            categoryId = categoryId,
            storeId = storeId,
            name = name.value,
            price = price.normalizedValue(),
            status = status,
            customizations = getCustomizations().map { customization ->
                customization.toData(
                    storeId = storeId,
                    offerId = id.value,
                    optionId = null,
                )
            },
        )
    }
}
