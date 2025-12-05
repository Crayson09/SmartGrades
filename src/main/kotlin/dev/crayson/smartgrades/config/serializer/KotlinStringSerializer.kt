package dev.crayson.smartgrades.config.serializer

import java.lang.String

class KotlinStringSerializer : de.exlll.configlib.Serializer<String,kotlin.String> {

    override fun serialize(p0: String?): kotlin.String {
        return p0.toString()
    }

    override fun deserialize(p0: kotlin.String?): String {
        return String(p0)
    }
}