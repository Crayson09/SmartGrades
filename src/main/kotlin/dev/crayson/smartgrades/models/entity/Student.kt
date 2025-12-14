@file:UseSerializers(UUIDSerializer::class)

package dev.crayson.smartgrades.models.entity

import dev.crayson.smartgrades.util.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

@Serializable
data class Student(
    @BsonId
    val studentId: UUID,
    val name: String,
    val email: String,
    @Transient
    val password: String = "",
    val `class`: String,
    val school: String,
)
