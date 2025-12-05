package dev.crayson.smartgrades

import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@GenerateOpenApi
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("idk")
        }
    }
}
