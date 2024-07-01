package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.exception.MalformedImagePathURLException
import java.net.URL

@JvmInline
value class Image(
    val path: String,
) {
    init {
        runCatching {
            URL(path)
        }.getOrElse { throw MalformedImagePathURLException(path) }
    }
}
