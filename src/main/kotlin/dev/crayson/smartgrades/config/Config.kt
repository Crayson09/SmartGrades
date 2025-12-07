package dev.crayson.smartgrades.config

@JvmRecord
data class Config(
    val mongoDBConnectionURL: String = "mongodb://root:password@localhost:27017/",
    val mongoDBDatabaseName: String = "smartGrades",
    val issuer: String = "smartgrades-api",
    val audience: String = "smartgrades",
    val secret: String = "4834gnregew90g3podqwpoüddqw90ir83oi3äö23f8923o",
)
