package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StoreDatastoreOutputPortMockTest {
    @Test
    fun `Should return true when is mock`() {
        val mock = StoreDatastoreOutputPortMock()

        mock.exists(Id()) shouldBe true
    }
}
