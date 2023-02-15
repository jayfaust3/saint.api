package com.saint.api.dataaccess.repositories

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import com.saint.api.common.models.data.saint.SaintDatabaseModel

interface SaintRepository : MongoRepository<SaintDatabaseModel, String> {
    fun findOneById(id: ObjectId): SaintDatabaseModel
}