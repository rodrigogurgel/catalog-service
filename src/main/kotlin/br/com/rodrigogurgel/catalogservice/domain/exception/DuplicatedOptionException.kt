package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DuplicatedOptionException(duplicatedOptionIds: List<Id>) :
    IllegalArgumentException(
        """Each Option can be only used one time.
Duplicated Option IDs: [$duplicatedOptionIds]
        """.trimMargin()
    )
