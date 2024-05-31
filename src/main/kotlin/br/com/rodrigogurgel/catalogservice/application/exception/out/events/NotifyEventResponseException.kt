package br.com.rodrigogurgel.catalogservice.application.exception.out.events

import org.apache.avro.generic.GenericRecord

data class NotifyEventResponseException(val record: GenericRecord) :
    RuntimeException(" Failed to notify event response. Record: [$record]")
