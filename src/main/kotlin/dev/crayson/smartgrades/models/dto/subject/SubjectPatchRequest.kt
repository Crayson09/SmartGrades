package dev.crayson.smartgrades.models.dto.subject

import dev.crayson.smartgrades.models.entity.SubjectType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SubjectPatchRequest(
    @Contextual
    val studentId: UUID? = null,
    val name: String? = null,
    val subjectType: SubjectType? = null,
)
