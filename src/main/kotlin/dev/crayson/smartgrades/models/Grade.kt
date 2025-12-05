package dev.crayson.smartgrades.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Grade (
    @SerialName("_id")
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
    PRESENTATION
}
