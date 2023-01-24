package com.saintapi.common.models.api.request.saint

import com.saintapi.common.enums.saint.Region

class PostSaintRequestModel (
    val active: Boolean = true,
    val name: String,
    val yearOfBirth: Int,
    val yearOfDeath: Int,
    val region: Region,
    val martyred: Boolean = false,
    val notes: String?,
    val hasAvatar: Boolean = false
)