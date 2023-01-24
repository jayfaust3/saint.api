package com.saintapi.common.models.dtos.saint

import java.time.LocalDateTime
import com.saintapi.common.enums.saint.Region

data class SaintDTO (
    val id: String,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val active: Boolean,
    val name: String,
    val yearOfBirth: Int,
    val yearOfDeath: Int,
    val region: Region,
    val martyred: Boolean,
    val notes: String?,
    val hasAvatar: Boolean?
)