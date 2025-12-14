package dev.crayson.smartgrades.models.dto.auth

import dev.crayson.smartgrades.models.entity.Student
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val student: Student,
    val token: String,
)
