package dev.crayson.smartgrades.models.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

@Serializable
data class Student(
    @BsonId
    @Contextual
    val studentId: UUID,
    val name: String,
    val `class`: String,
    val school: String,
)
