package com.saint.api.common.models.data.saint

class SaintKeyBuilder(id: String) {
    val partitionKey: String = "SAINT"
    val sortKey: String = "SAINT::$id"
}