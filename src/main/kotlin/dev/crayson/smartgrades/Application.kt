package dev.crayson.smartgrades

import dev.crayson.smartgrades.database.configureDatabases
import dev.crayson.smartgrades.routes.configureGradeRoutes
import dev.crayson.smartgrades.routes.configureStudentRoutes
import dev.crayson.smartgrades.routes.configureSubjectRoutes
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureDependencyInjection()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureStudentRoutes()
    configureSubjectRoutes()
    configureGradeRoutes()
}
