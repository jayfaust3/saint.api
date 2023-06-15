package com.saint.api.dataaccess.repositories

import org.springframework.beans.factory.annotation.Value
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.saint.api.configuration.AWSConfiguration
import com.permission.api.dataaccess.repositories.BaseDynamoDBRepository
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

    override fun get(request: SaintReadRequest): SaintDTO {
        val spec = GetItemSpec(
            PrimaryKey(
                "partitionKey",
                request.partitionKey,
                "sortKey",
                request.sortKey
            )
        )

        val getResult = tableClient.getItem(spec)

        return mapToDTO(getResult as SaintDatabaseModel)
    }

    override fun getAll(): List<SaintDTO> {
        val spec = QuerySpec().withHashKey(
            "partitionKey",
            "SAINT"
        )

        val queryResults = tableClient.query(spec)

        queryResults.map { item -> mapToDTO(item as SaintDatabaseModel) }
    }

    override fun upsert(request: SaintWriteRequest): SaintDTO {

    }

    override fun delete(request: SaintDeleteRequest): Unit {
        val spec = DeleteItemSpec()
            .withPrimaryKey(
                "partitionKey",
                request.partitionKey,
                "sortKey",
                request.sortKey
            )

        tableClient.deleteItem(spec)
    }

    private fun mapToDTO(dbModel: SaintDatabaseModel): SaintDTO {
        val id = dbModel.sortKey.replace("SAINT::", "")

        return SaintDTO(
            id,
            dbModel.createdDate,
            dbModel.modifiedDate,
            dbModel.active,
            dbModel.name,
            dbModel.yearOfBirth,
            dbModel.yearOfDeath,
            dbModel.region,
            dbModel.martyred,
            dbModel.notes,
            dbModel.hasAvatar
        )
    }
}