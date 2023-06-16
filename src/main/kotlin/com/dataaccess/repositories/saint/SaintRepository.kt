package com.saint.api.dataaccess.repositories

import org.springframework.beans.factory.annotation.Value
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.saint.api.configuration.AWSConfiguration
import com.permission.api.dataaccess.repositories.BaseDynamoDBRepository
import com.saint.api.common.enums.saint.Region
import com.saint.api.common.models.data.saint.SaintDatabaseModel
import com.saint.api.common.models.data.saint.SaintDeleteRequest
import com.saint.api.common.models.data.saint.SaintReadRequest
import com.saint.api.common.models.data.saint.SaintWriteRequest
import com.saint.api.common.models.dtos.SaintDTO

class SaintRepository (
    awsConfig: AWSConfiguration,
    @Value(value = "\${aws.dynamo.saintTableName}")
    private val tableName: String
) : BaseDynamoDBRepository(awsConfig), ISaintRepository {
    private val tableClient = super.client().getTable(tableName)

    override fun get(request: SaintReadRequest): SaintDTO? {
        val spec = GetItemSpec().withPrimaryKey(
            PrimaryKey(
                "partitionKey",
                request.partitionKey,
                "sortKey",
                request.sortKey
            )
        )

        val getResult = tableClient.getItem(spec)

        if (getResult == null) return null

        return mapToDTO(
            SaintDatabaseModel(
                getResult.get("partitionKey") as String,
                getResult.get("sortKey") as String,
                getResult.get("createdDate") as Long,
                getResult.get("modifiedDate") as Long,
                getResult.get("active") as Boolean,
                getResult.get("name") as String,
                getResult.get("yearOfBirth") as Int,
                getResult.get("yearOfDeath") as Int,
                getResult.get("region") as String,
                getResult.get("martyred") as Boolean,
                getResult.get("notes") as String?,
                getResult.get("hasAvatar") as Boolean
            )
        )
    }

    override fun getAll(): List<SaintDTO> {
        val spec = QuerySpec().withHashKey(
            "partitionKey",
            "SAINT"
        )

        val queryResults = tableClient.query(spec)

        return queryResults.map {
            item -> mapToDTO(
                SaintDatabaseModel(
                    item.get("partitionKey") as String,
                    item.get("sortKey") as String,
                    item.get("createdDate") as Long,
                    item.get("modifiedDate") as Long,
                    item.get("active") as Boolean,
                    item.get("name") as String,
                    item.get("yearOfBirth") as Int,
                    item.get("yearOfDeath") as Int,
                    item.get("region") as String,
                    item.get("martyred") as Boolean,
                    item.get("notes") as String?,
                    item.get("hasAvatar") as Boolean
                )
            )
        }
    }

    override fun upsert(request: SaintWriteRequest): SaintDTO {
        val record = request.record

        val item = Item()
            .withPrimaryKey(
                "partitionKey",
                record.partitionKey,
                "sortKey",
                record.sortKey
            )

        tableClient.putItem(item)

        return mapToDTO(record)
    }

    override fun delete(request: SaintDeleteRequest): Unit {
        val spec = GetItemSpec().withPrimaryKey(
            PrimaryKey(
                "partitionKey",
                request.partitionKey,
                "sortKey",
                request.sortKey
            )
        )

        val getResult = tableClient.getItem(spec)

        if (getResult != null) {
            if ((getResult.get("active") as Boolean)) {
                val recordToDelete = mapToDTO(
                    SaintDatabaseModel(
                        getResult.get("partitionKey") as String,
                        getResult.get("sortKey") as String,
                        getResult.get("createdDate") as Long,
                        getResult.get("modifiedDate") as Long,
                        false,
                        getResult.get("name") as String,
                        getResult.get("yearOfBirth") as Int,
                        getResult.get("yearOfDeath") as Int,
                        getResult.get("region") as String,
                        getResult.get("martyred") as Boolean,
                        getResult.get("notes") as String?,
                        getResult.get("hasAvatar") as Boolean
                    )
                )

                val deleteRequest = SaintWriteRequest(recordToDelete)

                upsert(deleteRequest)
            }
        }
    }

    private fun mapToDTO(dbModel: SaintDatabaseModel): SaintDTO {
        val id = dbModel.sortKey.replace("SAINT::", "")

        return SaintDTO(
            id,
            dbModel.createdDate,
            dbModel.modifiedDate,
            dbModel.name,
            dbModel.yearOfBirth,
            dbModel.yearOfDeath,
            Region.valueOf(dbModel.region),
            dbModel.martyred,
            dbModel.notes,
            dbModel.hasAvatar
        )
    }
}