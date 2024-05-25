package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.controller

import br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response.ItemResponseDTO
import br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.mapper.toResponseDTO
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.unwrap
import java.util.UUID
import kotlinx.coroutines.async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/items")
class ItemController(
    private val itemInputPort: ItemInputPort,
    private val optionInputPort: OptionInputPort,
    private val customizationInputPort: CustomizationInputPort,
    private val productInputPort: ProductInputPort,
) {

    @GetMapping("/{itemId}")
    suspend fun getItemById(
        @RequestHeader("Store-ID") storeId: UUID,
        @PathVariable(value = "itemId") itemId: UUID,
    ): ItemResponseDTO = coroutineBinding {
        val item = itemInputPort.find(storeId, itemId).bind()
        val reference = item.reference ?: throw RuntimeException("Item with id $itemId not found")

        val options = async { optionInputPort.searchByReferenceBeginsWith(storeId, reference).bind() }
        val customizations = async { customizationInputPort.searchByReferenceBeginsWith(storeId, reference).bind() }

        val productIds = options.await().map { option -> option.productId!! } + item.productId!!

        val products = productInputPort.find(storeId, productIds.toSet()).bind()

        val productsMap = products.associateBy { product -> product.productId!! }
        val customizationsMap = customizations.await()
            .groupBy { customization -> customization.reference!!.removeSuffix("#CUSTOMIZATION#${customization.customizationId}") }
        val optionsMap =
            options.await().groupBy { option -> option.reference!!.removeSuffix("#OPTION#${option.optionId}") }

        item.toResponseDTO(productsMap, customizationsMap, optionsMap)
    }.onFailure { throw it }
        .unwrap()
}