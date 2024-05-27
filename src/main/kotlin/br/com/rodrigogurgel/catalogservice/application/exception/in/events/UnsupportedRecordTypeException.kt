package br.com.rodrigogurgel.catalogservice.application.exception.`in`.events

import org.apache.avro.generic.GenericRecord

class UnsupportedRecordTypeException(record: GenericRecord) : RuntimeException(
    "No processor found for record: ${record::class.qualifiedName}"
)
