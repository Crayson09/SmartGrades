package dev.crayson.smartgrades

import dev.crayson.smartgrades.services.StudentService
import dev.crayson.smartgrades.services.SubjectService
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.configureDependencyInjection() {
    dependencies {
        provide<StudentService> { StudentService }
        provide<SubjectService> { SubjectService }
    }
}
