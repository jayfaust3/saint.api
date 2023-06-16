package com.saint.api.common.models.data.saint

import com.saint.api.common.models.dtos.SaintDTO

class SaintWriteRequest(saint: SaintDTO) {
    private val keys = SaintKeyBuilder(saint.id)
    val record = SaintDatabaseModel(
        keys.partitionKey,
        keys.sortKey,
        saint.createdDate,
        saint.modifiedDate,
        saint.active,
        saint.name,
        saint.yearOfBirth,
        saint.yearOfDeath,
        "${saint.region}",
        saint.martyred,
        saint.notes,
        saint.hasAvatar
    )
}
