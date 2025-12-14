package dev.crayson.smartgrades.models.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val `class`: String,
    val school: String,
)
