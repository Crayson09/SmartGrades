package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.models.dto.grade.GradeCreateRequest
import dev.crayson.smartgrades.models.dto.grade.GradePatchRequest
import dev.crayson.smartgrades.models.entity.Grade
import dev.crayson.smartgrades.services.GradeService
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
fun Application.configureGradeRoutes() {
    routing {
        val gradeService: GradeService by dependencies

        get("/api/grades/{gradeId}") {
            val gradeId = getUUID("gradeId")
            val grade = gradeService.getGrade(gradeId)
            if (grade != null) {
                call.respond(HttpStatusCode.OK, grade)
            } else {
                call.respond(HttpStatusCode.NotFound, "Grade not found")
            }
        }

        post("/api/grades") {
            val request = call.receive<GradeCreateRequest>()
            val grade = gradeService.createGrade(request)
            call.respond(HttpStatusCode.Created, grade)
        }

        patch("/api/grades/{gradeId}") {
            val gradeId = getUUID("gradeId")
            val gradePatch = call.receive<GradePatchRequest>()

            val oldGrade = gradeService.getGrade(gradeId)

            if (oldGrade != null) {
                val newGrade =
                    Grade(
                        gradeId,
                        subjectId = gradePatch.subjectId ?: oldGrade.subjectId,
                        value = gradePatch.value ?: oldGrade.value,
                        date = gradePatch.date ?: oldGrade.date,
                        type = gradePatch.type ?: oldGrade.type,
                    )
                gradeService.updateGrade(newGrade)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Grade not found")
            }
        }

        delete("/api/grades/{gradeId}") {
            val gradeId = getUUID("gradeId")
            val result = gradeService.deleteGrade(gradeId)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NoContent, "Grade not found")
            }
        }
    }
}
