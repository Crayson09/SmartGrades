package dev.crayson.smartgrades.models.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Student(
    @BsonId
    val studentId: String,
    val name: String,
    val `class`: String,
    val school: String,
)
