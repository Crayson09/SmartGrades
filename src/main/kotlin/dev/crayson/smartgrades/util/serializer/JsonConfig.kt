package dev.crayson.smartgrades.util.serializer

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.util.UUID

val json =
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        serializersModule =
            SerializersModule {
                contextual(UUID::class, UUIDSerializer)
            }
    }
