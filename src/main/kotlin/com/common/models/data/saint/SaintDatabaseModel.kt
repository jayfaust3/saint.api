package com.saint.api.common.models.data.saint

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import com.saint.api.common.enums.saint.Region

@Document(collection="saints")
data class SaintDatabaseModel (
        // @Id
        // override val id: ObjectId = ObjectId.get(),
        // override val createdDate: LocalDateTime = LocalDateTime.now(),
        // override val modifiedDate: LocalDateTime = LocalDateTime.now(),
        // override val active: Boolean = true,
        @Id
        val id: ObjectId = ObjectId.get(),
        val createdDate: Long,
        val modifiedDate: Long,
        val name: String,
        val yearOfBirth: Int,
        val yearOfDeath: Int,
        val region: Region,
        val martyred: Boolean = false,
        val notes: String?,
        val hasAvatar: Boolean = false
)
// : BaseDatabaseModel(id, createdDate, modifiedDate, active)