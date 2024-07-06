package br.com.rodrigogurgel.catalogservice.domain.exception

class MalformedImagePathURLException(path: String) :
    IllegalArgumentException("the Image path is invalid: $path")
