package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.models.dto.auth.LoginRequest
import dev.crayson.smartgrades.models.dto.auth.RegisterRequest
import dev.crayson.smartgrades.services.StudentService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAuthRoutes() {
    routing {
        val studentService: StudentService by dependencies

        post("/api/auth/login") {
            val request = call.receive<LoginRequest>()
            val login =
                studentService.loginStudent(request)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")

            call.respond(HttpStatusCode.OK, login)
        }

        post("/api/auth/register") {
            val request = call.receive<RegisterRequest>()
            val student =
                studentService.registerStudent(request)
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "Email already in use")

            call.respond(HttpStatusCode.Created, student)
        }
    }
}
