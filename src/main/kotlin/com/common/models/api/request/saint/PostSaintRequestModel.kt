package com.saint.api.common.models.api.request

import com.saint.api.common.enums.saint.Region

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