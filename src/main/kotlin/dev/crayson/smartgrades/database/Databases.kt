package dev.crayson.smartgrades.database

import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import dev.crayson.smartgrades.config.ConfigHandler
import io.ktor.server.application.*

private val defaultCodecs = MongoClientSettings.getDefaultCodecRegistry()
lateinit var mongoDatabase : MongoDatabase

fun Application.configureDatabases() {
   mongoDatabase = connectToMongoDB()
}

fun Application.connectToMongoDB(): MongoDatabase {

    val uri = ConfigHandler.config.mongoDBConnectionURL
    val databaseName = ConfigHandler.config.mongoDBDatabaseName

    val mongoClient = MongoClient.create(uri)
    val database = mongoClient.getDatabase(databaseName).withCodecRegistry(defaultCodecs)

    monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return database
}
