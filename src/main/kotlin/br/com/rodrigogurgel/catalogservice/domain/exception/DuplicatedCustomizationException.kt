package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DuplicatedCustomizationException(duplicatedCustomizationIds: List<Id>) :
    IllegalStateException(
        "Each customization can be only used one time. Duplicated Customization IDs: [$duplicatedCustomizationIds]"
    )
