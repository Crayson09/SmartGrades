package dev.crayson.smartgrades

import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

@GenerateOpenApi
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("idk")
        }
    }
}
