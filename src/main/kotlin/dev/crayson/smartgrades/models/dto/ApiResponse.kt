package dev.crayson.smartgrades.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val message: String,
)
