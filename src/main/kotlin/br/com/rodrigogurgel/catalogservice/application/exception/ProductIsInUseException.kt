package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class ProductIsInUseException(productId: Id, offerIds: List<Id>) :
    IllegalStateException(
        """the Product can't be deleted because it is in use.
Product Id [$productId] Offer Ids [$offerIds] 
        """.trimMargin()
    )
