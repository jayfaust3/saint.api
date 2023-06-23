package com.saint.api.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.data.mongodb.core.MongoClientFactoryBean
import com.mongodb.ConnectionString

@Component
class MongoDBConfiguration {
    @Bean
    fun mongo(
        @Value("\${mongodb.uri}") uri: String,
        @Value("\${mongodb.replicaSet}") replicaSet: String
    ): MongoClientFactoryBean {
        val mongo = MongoClientFactoryBean()
        mongo.setConnectionString(
            ConnectionString(uri)
        )
        mongo.setReplicaSet(replicaSet)
        return mongo
    }
}
