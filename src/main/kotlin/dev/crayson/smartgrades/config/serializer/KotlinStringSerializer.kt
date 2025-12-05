package dev.crayson.smartgrades.config.serializer

import java.lang.String

class KotlinStringSerializer : de.exlll.configlib.Serializer<String, kotlin.String> {
    override fun serialize(p0: String?): kotlin.String = p0.toString()

    override fun deserialize(p0: kotlin.String?): String = String(p0)
}
