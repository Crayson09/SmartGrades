package dev.crayson.smartgrades

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import dev.crayson.smartgrades.util.serializer.json as CustomJson

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(CustomJson)
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
