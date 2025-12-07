package dev.crayson.smartgrades.models.entity

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Subject(
    @BsonId
    val subjectId: String,
    val studentId: String,
    val name: String,
    val subjectTyp: SubjectType,
)

enum class SubjectType {
    MAJOR,
    MINOR,
}
