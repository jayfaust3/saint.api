package com.saint.api.common.models.data.saint

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
//import java.time.LocalDateTime
import java.time.OffsetDateTime
import com.saint.api.common.enums.saint.Region

//@Document(collection="saints")
//data class SaintDatabaseModel (
//        // @Id
//        // override val id: ObjectId = ObjectId.get(),
//        // override val createdDate: LocalDateTime = LocalDateTime.now(),
//        // override val modifiedDate: LocalDateTime = LocalDateTime.now(),
//        // override val active: Boolean = true,
//        @Id
//        val id: ObjectId = ObjectId.get(),
//        val createdDate: LocalDateTime = LocalDateTime.now(),
//        val modifiedDate: LocalDateTime = LocalDateTime.now(),
//        val active: Boolean = true,
//        val name: String,
//        val yearOfBirth: Int,
//        val yearOfDeath: Int,
//        val region: Region,
//        val martyred: Boolean = false,
//        val notes: String?,
//        val hasAvatar: Boolean = false
//)
// : BaseDatabaseModel(id, createdDate, modifiedDate, active)

data class SaintDatabaseModel (
        val partitionKey: String,
        val sortKey: String,
        val createdDate: OffsetDateTime,
        val modifiedDate: OffsetDateTime,
        val active: Boolean,
        val name: String,
        val yearOfBirth: Int,
        val yearOfDeath: Int,
        val region: Region,
        val martyred: Boolean,
        val notes: String?,
        val hasAvatar: Boolean
)