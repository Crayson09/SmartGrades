package dev.crayson.smartgrades.models.dto.student

import kotlinx.serialization.Serializable

@Serializable
data class StudentPatchRequest(
    val name: String? = null,
    val `class`: String? = null,
    val school: String? = null,
)
