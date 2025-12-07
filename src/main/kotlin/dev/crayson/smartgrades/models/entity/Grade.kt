package dev.crayson.smartgrades.models.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Grade(
    @BsonId
    val id: String,
    val subjectId: String,
    val value: Int,
    val date: String,
    val type: GradeType,
)

@Serializable
enum class GradeType {
    ORAL,
    EXAM,
    QUIZ,
    PRESENTATION,
}
