package com.saint.api.dataaccess.repositories

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Value
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.permission.api.dataaccess.repositories.BaseDynamoDBRepository
import com.saint.api.configuration.AWSConfiguration

@Repository
class DynamoSaintRepository (
    awsConfig: AWSConfiguration,
    @Value(value = "\${aws.dynamo.saintTableName}")
    private val saintTableName: String
) : BaseDynamoDBRepository(awsConfig), ISaintRepository {
    private val tableClient = super.client().getTable(saintTableName)


}