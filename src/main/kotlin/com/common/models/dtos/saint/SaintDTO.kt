package com.saint.api.common.models.dtos

import java.time.OffsetDateTime
import com.saint.api.common.enums.saint.Region

data class SaintDTO (
    val id: String,
    val name: String,
    val yearOfBirth: Int,
    val yearOfDeath: Int,
    val region: Region,
    val martyred: Boolean,
    val notes: String?,
    val hasAvatar: Boolean?
)