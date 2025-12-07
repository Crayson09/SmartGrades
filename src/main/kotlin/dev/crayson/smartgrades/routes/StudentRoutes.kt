package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.getUUID
import dev.crayson.smartgrades.models.dto.student.StudentCreateRequest
import dev.crayson.smartgrades.models.entity.Student
import dev.crayson.smartgrades.services.StudentService
import dev.crayson.smartgrades.services.SubjectService
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
        val subjectService: SubjectService by dependencies

        get("/api/students") {
            val students = studentService.getAllStudents()
            call.respond(HttpStatusCode.OK, students)
        }

        get("/api/students/{studentId}") {
            val uuid = getUUID("studentId")
            val student = studentService.getStudent(uuid)

            if (student != null) {
                call.respond(HttpStatusCode.OK, student)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/api/students/{studentId}/subjects") {
            val uuid = getUUID("studentId")
            val subjects = subjectService.getAllSubjects(uuid)
            call.respond(HttpStatusCode.OK, subjects)
        }

        post("/api/students") {
            val student = call.receive<StudentCreateRequest>()
            studentService.createStudent(student)
            call.respond(HttpStatusCode.Created)
        }

        patch("/api/students/{studentId}") {
            val studentId = getUUID("studentId")
            val student = call.receive<Student>()

            if (studentId.toString() != student.studentId) {
                return@patch call.respond(HttpStatusCode.BadRequest)
            }

            val result = studentService.updateStudent(student)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/api/students/{studentId}") {
            val uuid = getUUID("studentId")
            val result = studentService.deleteStudent(uuid)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
