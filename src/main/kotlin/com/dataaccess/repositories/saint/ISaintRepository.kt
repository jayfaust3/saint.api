package com.saint.api.dataaccess.repositories

import com.saint.api.common.models.data.saint.SaintDatabaseModel
import com.saint.api.common.models.data.saint.SaintDeleteRequest
import com.saint.api.common.models.data.saint.SaintReadRequest
import com.saint.api.common.models.data.saint.SaintWriteRequest
import com.saint.api.common.models.dtos.SaintDTO

interface ISaintRepository {
    fun get(readRequest: SaintReadRequest): SaintDTO

    fun getAll(): List<SaintDTO>

    fun upsert(writeRequest: SaintWriteRequest): SaintDTO

    fun delete(deleteRequest: SaintDeleteRequest): Unit
}