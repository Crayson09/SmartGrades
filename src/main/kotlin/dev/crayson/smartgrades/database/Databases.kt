package dev.crayson.smartgrades.database

import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import dev.crayson.smartgrades.config.ConfigHandler
import dev.crayson.smartgrades.database.codec.MongoDBUUIDCodec
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import org.bson.codecs.configuration.CodecRegistries

private val defaultCodecs = MongoClientSettings.getDefaultCodecRegistry()
lateinit var mongoDatabase: MongoDatabase

private val customCodecs = CodecRegistries.fromCodecs(MongoDBUUIDCodec(defaultCodecs))

private val codecs = CodecRegistries.fromRegistries(customCodecs, defaultCodecs)

fun Application.configureDatabases() {
    mongoDatabase = connectToMongoDB()
}

fun Application.connectToMongoDB(): MongoDatabase {
    val uri = ConfigHandler.config.mongoDBConnectionURL
    val databaseName = ConfigHandler.config.mongoDBDatabaseName

    val mongoClient = MongoClient.create(uri)
    val database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecs)

    monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return database
}
