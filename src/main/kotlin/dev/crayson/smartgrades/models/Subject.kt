package dev.crayson.smartgrades.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    @SerialName("_id")
    val id: String,
    val studentId: String,
    val name: String,
    val subjectTyp: SubjectType,
)

enum class SubjectType {
    MAJOR,
    MINOR,
}