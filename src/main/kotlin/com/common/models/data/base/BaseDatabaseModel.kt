package com.saintapi.common.models.data.base

import org.bson.types.ObjectId
import java.time.LocalDateTime

abstract class BaseDatabaseModel (
        open val id: ObjectId,
        open val createdDate: LocalDateTime,
        open val modifiedDate: LocalDateTime,
        open val active: Boolean
)