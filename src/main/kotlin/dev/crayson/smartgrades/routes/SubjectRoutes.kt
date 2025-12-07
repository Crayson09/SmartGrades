package dev.crayson.smartgrades.routes

import dev.crayson.smartgrades.getUUID
import dev.crayson.smartgrades.models.dto.subject.SubjectCreateRequest
import dev.crayson.smartgrades.models.dto.subject.SubjectPatchRequest
import dev.crayson.smartgrades.models.entity.Subject
import dev.crayson.smartgrades.services.GradeService
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
fun Application.configureSubjectRoutes() {
    routing {
        val subjectService: SubjectService by dependencies
        val gradeService: GradeService by dependencies

        get("/api/subjects/{subjectId}") {
            val uuid = getUUID("subjectId")
            val subject = subjectService.getSubject(uuid)
            if (subject != null) {
                call.respond(HttpStatusCode.OK, subject)
            } else {
                call.respond(HttpStatusCode.NotFound, "Subject not found")
            }
        }

        get("/api/subjects/{subjectId}/grades") {
            val subjectId = getUUID("subjectId")
            val grades = gradeService.getAllGrades(subjectId)
            call.respond(HttpStatusCode.OK, grades)
        }

        post("/api/subjects") {
            val request = call.receive<SubjectCreateRequest>()
            subjectService.createSubject(request)
            call.respond(HttpStatusCode.Created)
        }

        patch("/api/subjects/{subjectId}") {
            val subjectId = getUUID("subjectId")
            val subjectPatch = call.receive<SubjectPatchRequest>()

            val oldSubject = subjectService.getSubject(subjectId)

            if (oldSubject != null) {
                val newSubject =
                    Subject(
                        subjectId,
                        studentId = subjectPatch.studentId ?: oldSubject.studentId,
                        name = subjectPatch.name ?: oldSubject.name,
                        subjectType = subjectPatch.subjectType ?: oldSubject.subjectType,
                    )
                subjectService.updateSubject(newSubject)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Subject not found")
            }
        }

        delete("/api/subjects/{subjectId}") {
            val uuid = getUUID("subjectId")
            val result = subjectService.deleteSubject(uuid)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Subject not found")
            }
        }
    }
}
