package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.getUUID
import dev.crayson.smartgrades.models.Student
import dev.crayson.smartgrades.models.StudentCreateRequest
import dev.crayson.smartgrades.services.StudentService
import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

@GenerateOpenApi
fun Application.configureStudentRoutes() {
    routing {
        val studentService: StudentService by dependencies

        get("/api/students") {
            val students = studentService.getAllStudents()
            if (students.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, students)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }

        get("/api/students/{uuid}") {
            val uuid = getUUID()
            val student = studentService.getStudent(uuid)

            if (student != null) {
                call.respond(HttpStatusCode.OK, student)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/api/students") {
            val student = call.receive<StudentCreateRequest>()
            studentService.createStudent(student)
            call.respond(HttpStatusCode.Created)
        }

        patch("/api/students") {
            val student = call.receive<Student>()
            val result = studentService.updateStudent(student)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/api/students/{uuid}") {
            val uuid = getUUID()
            val result = studentService.deleteStudent(uuid)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
