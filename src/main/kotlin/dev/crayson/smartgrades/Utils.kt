package dev.crayson.smartgrades

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import java.util.UUID

suspend fun RoutingContext.getUUID(): UUID {
    val uuidParam =
        call.parameters["uuid"]
            ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing UUID")
                throw IllegalArgumentException("Missing UUID")
            }

    return try {
        UUID.fromString(uuidParam)
    } catch (e: IllegalArgumentException) {
        call.respond(HttpStatusCode.BadRequest, "Invalid UUID")
        throw IllegalArgumentException("Invalid UUID")
    }
}
