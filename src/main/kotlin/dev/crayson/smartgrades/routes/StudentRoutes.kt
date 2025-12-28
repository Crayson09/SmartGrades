package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.models.dto.ApiResponse
import dev.crayson.smartgrades.models.dto.student.StudentPatchRequest
import dev.crayson.smartgrades.models.entity.GradeType
import dev.crayson.smartgrades.models.entity.Student
import dev.crayson.smartgrades.models.entity.Subject
import dev.crayson.smartgrades.services.GradeService
import dev.crayson.smartgrades.services.StudentService
import dev.crayson.smartgrades.services.SubjectService
import dev.crayson.smartgrades.util.PasswordManager
import dev.crayson.smartgrades.util.getUUID
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.routing

fun Application.configureStudentRoutes() {
    routing {
        authenticate("auth-jwt") {
            val studentService: StudentService by dependencies
            val subjectService: SubjectService by dependencies
            val gradeService: GradeService by dependencies

            get("/api/students/{studentId}") {
                val studentId = getUUID("studentId")
                val student =
                    studentService.getStudent(studentId)
                        ?: return@get call.respond(HttpStatusCode.NotFound, ApiResponse("Student not found"))

                call.respond(HttpStatusCode.OK, student)
            }

            get("/api/students/{studentId}/subjects") {
                val studentId = getUUID("studentId")
                val subjects = subjectService.getAllSubjects(studentId)

                call.respond(HttpStatusCode.OK, subjects)
            }

            get("/api/students/{studentId}/subjects/{subjectId}/average") {
                val studentId = getUUID("studentId")
                val subjectId = getUUID("subjectId")

                val subject = subjectService.getSubject(subjectId)
                    ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse("Subject not found")
                    )

                if (subject.studentId != studentId) {
                    return@get call.respond(
                        HttpStatusCode.Forbidden,
                        ApiResponse("Subject does not belong to student")
                    )
                }

                val grades = gradeService.getAllGrades(subjectId)
                if (grades.isEmpty()) {
                    return@get call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse("No grades found")
                    )
                }

                val smallGrades = grades.filter {
                    it.type == GradeType.ORAL ||
                            it.type == GradeType.QUIZ ||
                            it.type == GradeType.PRESENTATION
                }

                val examGrades = grades.filter { it.type == GradeType.EXAM }

                if (smallGrades.isEmpty() || examGrades.isEmpty()) {
                    return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse("Not enough grades to calculate average")
                    )
                }

                val smallAverage = smallGrades.map { it.value }.average()
                val examAverage = examGrades.map { it.value }.average()

                val finalAverage = (smallAverage + examAverage * 2) / 3

                call.respond(HttpStatusCode.OK, finalAverage)
            }


            patch("/api/students/{studentId}") {
                val studentId = getUUID("studentId")
                val patch = call.receive<StudentPatchRequest>()

                val old =
                    studentService.getStudent(studentId)
                        ?: return@patch call.respond(HttpStatusCode.NotFound, ApiResponse("Student not found"))

                val newPassword = patch.password?.let { PasswordManager.hashPassword(it) } ?: old.password

                val updated =
                    old.copy(
                        name = patch.name ?: old.name,
                        `class` = patch.`class` ?: old.`class`,
                        school = patch.school ?: old.school,
                        email = patch.email ?: old.email,
                        password = newPassword,
                    )

                studentService.updateStudent(updated)
                call.respond(HttpStatusCode.OK, ApiResponse("Student updated"))
            }

            delete("/api/students/{studentId}") {
                val studentId = getUUID("studentId")
                val deleted = studentService.deleteStudent(studentId)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, ApiResponse("Student deleted"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse("Student not found"))
                }
            }
        }
    }
}
