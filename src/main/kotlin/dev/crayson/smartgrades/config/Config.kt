package dev.crayson.smartgrades.config

@JvmRecord
data class Config(
    val mongoDBConnectionURL: String = "mongodb://root:password@localhost:27017/",
    val mongoDBDatabaseName: String = "smartGrades",
)