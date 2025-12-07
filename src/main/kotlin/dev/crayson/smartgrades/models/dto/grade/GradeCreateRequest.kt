package dev.crayson.smartgrades.models.dto.grade

import dev.crayson.smartgrades.models.entity.GradeType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GradeCreateRequest(
    @Contextual
    val subjectId: UUID,
    val value: Int,
    val date: String,
    val type: GradeType,
)
