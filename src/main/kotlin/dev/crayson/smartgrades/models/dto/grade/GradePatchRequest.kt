package dev.crayson.smartgrades.models.dto.grade

import dev.crayson.smartgrades.models.entity.GradeType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GradePatchRequest(
    @Contextual
    val subjectId: UUID? = null,
    val value: Int? = null,
    val date: String? = null,
    val type: GradeType? = null,
)
