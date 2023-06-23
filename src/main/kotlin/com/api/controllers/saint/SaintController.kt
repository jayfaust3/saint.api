package com.saint.api.api

import com.saint.api.common.models.data.saint.SaintDatabaseModel
import com.saint.api.dataaccess.repositories.SaintRepository
import com.saint.api.common.models.api.response.ResponseModel
import com.saint.api.common.models.api.request.PostSaintRequestModel
import com.saint.api.common.models.dtos.SaintDTO
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime

@RestController
@RequestMapping("/api/saints")
class SaintController(
        private val repository: SaintRepository
) {

    @GetMapping
    fun getAll(): ResponseEntity<ResponseModel<List<SaintDTO>>> {
        val records = repository.findAll().map{ it -> 
            SaintDTO(
                id = it.id.toString(),
                name = it.name,
                yearOfBirth = it.yearOfBirth,
                yearOfDeath = it.yearOfDeath,
                region = it.region,
                martyred = it.martyred,
                notes = it.notes,
                hasAvatar = it.hasAvatar
            )
        }

        return ResponseEntity.ok(ResponseModel(records))
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<ResponseModel<SaintDTO>> {
        val record = repository.findOneById(ObjectId(id))

        return ResponseEntity.ok(
            ResponseModel(
                SaintDTO(
                    id = record.id.toString(),
                    name = record.name,
                    yearOfBirth = record.yearOfBirth,
                    yearOfDeath = record.yearOfDeath,
                    region = record.region,
                    martyred = record.martyred,
                    notes = record.notes,
                    hasAvatar = record.hasAvatar
                )
            )
        )
    }

    @PostMapping
    fun create(@RequestBody request: PostSaintRequestModel): ResponseEntity<ResponseModel<SaintDTO>> {
        val record = repository.save(
            SaintDatabaseModel(
                createdDate = OffsetDateTime.now().toEpochSecond(),
                modifiedDate = OffsetDateTime.now().toEpochSecond(),
                name = request.name,
                yearOfBirth = request.yearOfBirth,
                yearOfDeath = request.yearOfDeath,
                region = request.region,
                martyred = request.martyred,
                notes = request.notes,
                hasAvatar = request.hasAvatar
            )
        )

        return ResponseEntity(
            ResponseModel(
                SaintDTO(
                    id = record.id.toString(),
                    name = record.name,
                    yearOfBirth = record.yearOfBirth,
                    yearOfDeath = record.yearOfDeath,
                    region = record.region,
                    martyred = record.martyred,
                    notes = record.notes,
                    hasAvatar = record.hasAvatar
                )
            ), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody request: PostSaintRequestModel, @PathVariable("id") id: String): ResponseEntity<ResponseModel<SaintDTO>> {
        val record = repository.findOneById(ObjectId(id))

        val updatedRecord = repository.save(
            SaintDatabaseModel(
                id = record.id,
                createdDate = record.createdDate,
                modifiedDate = OffsetDateTime.now().toEpochSecond(),
                name = request.name,
                yearOfBirth = request.yearOfBirth,
                yearOfDeath = request.yearOfDeath,
                region = request.region,
                martyred = request.martyred,
                notes = request.notes,
                hasAvatar = request.hasAvatar
            )
        )

        return ResponseEntity.ok(
            ResponseModel(
                SaintDTO(
                    id = updatedRecord.id.toString(),
                    name = updatedRecord.name,
                    yearOfBirth = updatedRecord.yearOfBirth,
                    yearOfDeath = updatedRecord.yearOfDeath,
                    region = updatedRecord.region,
                    martyred = updatedRecord.martyred,
                    notes = updatedRecord.notes,
                    hasAvatar = updatedRecord.hasAvatar
                )
            )
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<ResponseModel<Boolean>> {
        val record = repository.findOneById(ObjectId(id))

        if (record !== null) repository.deleteById(id)

        return ResponseEntity.ok(
            ResponseModel(true)
        )
    }
}