@file:UseSerializers(UUIDSerializer::class)

package dev.crayson.smartgrades.models.entity

import dev.crayson.smartgrades.util.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

@Serializable
data class Grade(
    @BsonId
    val gradeId: UUID,
    val subjectId: UUID,
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
