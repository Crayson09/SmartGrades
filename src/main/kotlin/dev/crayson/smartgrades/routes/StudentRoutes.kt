package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.models.dto.student.StudentCreateRequest
import dev.crayson.smartgrades.models.dto.student.StudentPatchRequest
import dev.crayson.smartgrades.models.entity.Student
import dev.crayson.smartgrades.services.GradeService
import dev.crayson.smartgrades.services.StudentService
import dev.crayson.smartgrades.services.SubjectService
import dev.crayson.smartgrades.util.getUUID
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
        val gradeService: GradeService by dependencies

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

        get("/api/students/{studentId}/subjects/{subjectId}/average") {
            val studentId = getUUID("studentId")
            val subjectId = getUUID("subjectId")

            val subject = subjectService.getSubject(subjectId)
            if (subject == null || subject.studentId != studentId) {
                call.respond(HttpStatusCode.Forbidden, "Subject does not belong to student")
                return@get
            }

            val grades = gradeService.getAllGrades(subjectId)

            if (grades.isEmpty()) {
                call.respond(HttpStatusCode.OK, 0.0)
                return@get
            }

            val average = grades.map { it.value }.average()

            call.respond(HttpStatusCode.OK, average)
        }

        post("/api/students") {
            val student = call.receive<StudentCreateRequest>()
            studentService.createStudent(student)
            call.respond(HttpStatusCode.Created)
        }

        patch("/api/students/{studentId}") {
            val studentId = getUUID("studentId")
            val studentPatch = call.receive<StudentPatchRequest>()

            val oldStudent = studentService.getStudent(studentId)

            if (oldStudent != null) {
                val newStudent =
                    Student(
                        studentId,
                        name = studentPatch.name ?: oldStudent.name,
                        `class` = studentPatch.`class` ?: oldStudent.`class`,
                        school = studentPatch.school ?: oldStudent.school,
                    )
                studentService.updateStudent(newStudent)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Student not found")
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
