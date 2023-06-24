package com.saint.api.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.saint.api.dataaccess.repositories"])
class MongoConfig(@Value("\${mongodb.uri}")
                  private val uri: String,
                  @Value("\${mongodb.saintDBName}")
                  private val saintDBName: String,
                  @Value("\${mongodb.replicaSet}")
                  private val replicaSet: String) : AbstractMongoClientConfiguration() {

                      override fun getDatabaseName(): String {
        return saintDBName
    }

    @Bean
    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString("$uri/$saintDBName")

        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()

        return MongoClients.create(settings)
    }

    @Bean
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClient(), databaseName)
    }
}
