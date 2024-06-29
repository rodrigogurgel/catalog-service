package br.com.rodrigogurgel.catalogservice.domain.vo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.net.MalformedURLException

class ImageTest {
    @Test
    fun `Should instantiate Image with success when path is a valid url`() {
        val path = "https://www.example.com"
        val image = Image(path)

        image.path shouldBe path
    }

    @Test
    fun `Should instantiate Image with error when path is not a valid url`() {
        val path = "www.example.com"
        shouldThrow<MalformedURLException> {
            Image(path)
        }
    }
}
