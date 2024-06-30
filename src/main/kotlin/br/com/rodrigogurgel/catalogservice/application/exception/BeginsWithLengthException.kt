package br.com.rodrigogurgel.catalogservice.application.exception

class BeginsWithLengthException(beginsWithLength: Int) :
    IllegalArgumentException(
        "The begins with should have length at least 3 characters. " +
            "Actual length is $beginsWithLength"
    )
