package com.saint.api.api

import java.time.OffsetDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.saint.api.dataaccess.repositories.SaintRepository
import com.saint.api.common.models.api.response.ResponseModel
import com.saint.api.common.models.api.request.PostSaintRequestModel
import com.saint.api.common.models.data.saint.SaintReadRequest
import com.saint.api.common.models.data.saint.SaintWriteRequest
import com.saint.api.common.models.dtos.SaintDTO
import java.util.*

@RestController
@RequestMapping("/api/saints")
class SaintController(
        private val repository: SaintRepository
) {

    @GetMapping
    fun getAll(): ResponseEntity<ResponseModel<List<SaintDTO>>> {
        val records = repository.getAll()

        return ResponseEntity.ok(ResponseModel(records))
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<ResponseModel<SaintDTO?>> {
        val record = repository.get(SaintReadRequest(id)) ?: return ResponseEntity(ResponseModel(null), HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(ResponseModel(record))
    }

    @PostMapping
    fun create(@RequestBody request: PostSaintRequestModel): ResponseEntity<ResponseModel<SaintDTO>> {
        val record = repository.upsert(
            SaintWriteRequest(
                SaintDTO(
                    UUID.randomUUID().toString(),
                    OffsetDateTime.now().toEpochSecond(),
                    OffsetDateTime.now().toEpochSecond(),
                    request.name,
                    request.yearOfBirth,
                    request.yearOfDeath,
                    request.region,
                    request.martyred,
                    request.notes,
                    request.hasAvatar
                )
            )
        )

        return ResponseEntity(ResponseModel(record), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody request: PostSaintRequestModel, @PathVariable("id") id: String): ResponseEntity<ResponseModel<SaintDTO?>> {
        val record = repository.get(SaintReadRequest(id)) ?: return ResponseEntity(ResponseModel(null), HttpStatus.NOT_FOUND)

        val updatedRecord = repository.upsert(
            SaintWriteRequest(
                SaintDTO(
                    id,
                    record.createdDate,
                    OffsetDateTime.now().toEpochSecond(),
                    request.name,
                    request.yearOfBirth,
                    request.yearOfDeath,
                    request.region,
                    request.martyred,
                    request.notes,
                    request.hasAvatar
                )
            )
        )

        return ResponseEntity.ok(ResponseModel(updatedRecord))
    }
}