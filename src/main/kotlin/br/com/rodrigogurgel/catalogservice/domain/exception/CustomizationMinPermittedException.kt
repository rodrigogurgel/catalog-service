package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CustomizationMinPermittedException(customizationId: Id) :
    IllegalStateException(
        """Customization $customizationId has minimum permitted is grater then quantity of options with status available size
        """.trimIndent()
    )
