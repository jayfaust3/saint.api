package com.saintapi.dataaccess.repositories.saint

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import com.saintapi.common.models.data.saint.SaintDatabaseModel

interface SaintRepository : MongoRepository<SaintDatabaseModel, String> {
    fun findOneById(id: ObjectId): SaintDatabaseModel
}