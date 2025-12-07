package dev.crayson.smartgrades.models.dto.student

import kotlinx.serialization.Serializable

@Serializable
data class StudentCreateRequest(
    val name: String,
    val `class`: String,
    val school: String,
)
