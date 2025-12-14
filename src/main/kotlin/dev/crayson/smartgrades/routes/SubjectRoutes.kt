package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.models.dto.ApiResponse
import dev.crayson.smartgrades.models.dto.subject.SubjectCreateRequest
import dev.crayson.smartgrades.models.dto.subject.SubjectPatchRequest
import dev.crayson.smartgrades.services.GradeService
import dev.crayson.smartgrades.services.SubjectService
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
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureSubjectRoutes() {
    routing {
        authenticate("auth-jwt") {
            val subjectService: SubjectService by dependencies
            val gradeService: GradeService by dependencies

            get("/api/subjects/{subjectId}") {
                val subjectId = getUUID("subjectId")
                val subject =
                    subjectService.getSubject(subjectId)
                        ?: return@get call.respond(HttpStatusCode.NotFound, ApiResponse("Subject not found"))

                call.respond(HttpStatusCode.OK, subject)
            }

            get("/api/subjects/{subjectId}/grades") {
                val subjectId = getUUID("subjectId")
                val grades = gradeService.getAllGrades(subjectId)

                call.respond(HttpStatusCode.OK, grades)
            }

            post("/api/subjects") {
                val request = call.receive<SubjectCreateRequest>()
                val subject = subjectService.createSubject(request)

                call.respond(HttpStatusCode.Created, subject)
            }

            patch("/api/subjects/{subjectId}") {
                val subjectId = getUUID("subjectId")
                val patch = call.receive<SubjectPatchRequest>()

                val old =
                    subjectService.getSubject(subjectId)
                        ?: return@patch call.respond(HttpStatusCode.NotFound, ApiResponse("Subject not found"))

                val updated =
                    old.copy(
                        studentId = patch.studentId ?: old.studentId,
                        name = patch.name ?: old.name,
                        subjectType = patch.subjectType ?: old.subjectType,
                    )

                subjectService.updateSubject(updated)
                call.respond(HttpStatusCode.OK, ApiResponse("Subject updated"))
            }

            delete("/api/subjects/{subjectId}") {
                val subjectId = getUUID("subjectId")
                val deleted = subjectService.deleteSubject(subjectId)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, ApiResponse("Subject deleted"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse("Subject not found"))
                }
            }
        }
    }
}
