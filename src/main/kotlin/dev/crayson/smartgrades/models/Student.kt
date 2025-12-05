package dev.crayson.smartgrades.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Student (
    @SerialName("_id")
    val id: String,
    val name: String,
    val `class`: String,
    val school: String,
)