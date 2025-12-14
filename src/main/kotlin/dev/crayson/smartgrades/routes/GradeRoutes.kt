package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.models.dto.ApiResponse
import dev.crayson.smartgrades.models.dto.grade.GradeCreateRequest
import dev.crayson.smartgrades.models.dto.grade.GradePatchRequest
import dev.crayson.smartgrades.models.entity.Grade
import dev.crayson.smartgrades.services.GradeService
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

fun Application.configureGradeRoutes() {
    routing {
        authenticate("auth-jwt") {
            val gradeService: GradeService by dependencies

            get("/api/grades/{gradeId}") {
                val gradeId = getUUID("gradeId")
                val grade =
                    gradeService.getGrade(gradeId)
                        ?: return@get call.respond(HttpStatusCode.NotFound, ApiResponse("Grade not found"))

                call.respond(HttpStatusCode.OK, grade)
            }

            post("/api/grades") {
                val request = call.receive<GradeCreateRequest>()
                val grade = gradeService.createGrade(request)
                call.respond(HttpStatusCode.Created, grade)
            }

            patch("/api/grades/{gradeId}") {
                val gradeId = getUUID("gradeId")
                val gradePatch = call.receive<GradePatchRequest>()

                val oldGrade =
                    gradeService.getGrade(gradeId)
                        ?: return@patch call.respond(HttpStatusCode.NotFound, ApiResponse("Grade not found"))

                val newGrade =
                    oldGrade.copy(
                        subjectId = gradePatch.subjectId ?: oldGrade.subjectId,
                        value = gradePatch.value ?: oldGrade.value,
                        date = gradePatch.date ?: oldGrade.date,
                        type = gradePatch.type ?: oldGrade.type,
                    )

                gradeService.updateGrade(newGrade)
                call.respond(HttpStatusCode.OK, ApiResponse("Grade updated"))
            }

            delete("/api/grades/{gradeId}") {
                val gradeId = getUUID("gradeId")
                val deleted = gradeService.deleteGrade(gradeId)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, ApiResponse("Grade deleted"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse("Grade not found"))
                }
            }
        }
    }
}
