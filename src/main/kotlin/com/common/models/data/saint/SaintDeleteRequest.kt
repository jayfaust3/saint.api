package com.saint.api.common.models.data.saint

class SaintDeleteRequest(id: String) {
    private val keys = SaintKeyBuilder(id)
    val partitionKey: String = keys.partitionKey
    val sortKey: String = keys.sortKey
}