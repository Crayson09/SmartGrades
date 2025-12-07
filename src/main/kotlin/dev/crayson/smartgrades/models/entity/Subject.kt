package dev.crayson.smartgrades.models.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

@Serializable
data class Subject(
    @BsonId
    @Contextual
    val subjectId: UUID,
    val studentId: UUID,
    val name: String,
    val subjectType: SubjectType,
)

enum class SubjectType {
    MAJOR,
    MINOR,
}
