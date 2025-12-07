package dev.crayson.smartgrades.models.dto.subject

import dev.crayson.smartgrades.models.entity.SubjectType
import kotlinx.serialization.Serializable

@Serializable
data class SubjectCreateRequest(
    val studentId: String,
    val name: String,
    val subjectType: SubjectType,
)
