package dev.crayson.smartgrades.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Subject(
    @BsonId
    val id: String,
    val studentId: String,
    val name: String,
    val subjectTyp: SubjectType,
)

enum class SubjectType {
    MAJOR,
    MINOR,
}
