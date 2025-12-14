package dev.crayson.smartgrades.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.crayson.smartgrades.config.ConfigHandler
import dev.crayson.smartgrades.database.mongoDatabase
import dev.crayson.smartgrades.models.dto.auth.LoginRequest
import dev.crayson.smartgrades.models.dto.auth.LoginResponse
import dev.crayson.smartgrades.models.dto.auth.RegisterRequest
import dev.crayson.smartgrades.models.entity.Student
import dev.crayson.smartgrades.util.PasswordManager
import dev.crayson.smartgrades.util.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import java.util.UUID

object StudentService {
    private val collection: MongoCollection<Student> = mongoDatabase.getCollection<Student>("students")

    suspend fun loginStudent(request: LoginRequest): LoginResponse? {
        val student = collection.find(Filters.eq("email", request.email)).firstOrNull() ?: return null
        val correctPassword = PasswordManager.comparePassword(request.password, student.password)

        return if (correctPassword) {
            val token = TokenManager.generateToken(student.studentId)

            LoginResponse(
                token = token,
                student = student,
            )
        } else {
            null
        }
    }

    suspend fun getStudent(uuid: UUID) = collection.find(Filters.eq("_id", uuid)).firstOrNull()

    suspend fun registerStudent(request: RegisterRequest): Student? {
        if (collection.find(Filters.eq("email", request.email)).firstOrNull() != null) return null

        val student =
            Student(
                name = request.name,
                email = request.email,
                `class` = request.`class`,
                studentId = UUID.randomUUID(),
                school = request.school,
                password = PasswordManager.hashPassword(request.password),
            )
        collection.insertOne(student)
        return student
    }

    suspend fun updateStudent(student: Student): Boolean {
        val result =
            collection.replaceOne(
                Filters.eq("_id", student.studentId),
                student,
                ReplaceOptions().upsert(false),
            )
        return result.modifiedCount > 0
    }

    suspend fun deleteStudent(uuid: UUID): Boolean {
        val result = collection.deleteOne(Filters.eq("_id", uuid))
        return result.deletedCount > 0
    }
}
