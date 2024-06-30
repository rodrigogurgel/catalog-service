package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController {
    fun categories() = emptyList<Any>()
}
