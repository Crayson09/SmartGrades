package dev.crayson.smartgrades.services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.crayson.smartgrades.database.mongoDatabase
import dev.crayson.smartgrades.models.dto.student.StudentCreateRequest
import dev.crayson.smartgrades.models.entity.Student
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.UUID

object StudentService {
    private val collection: MongoCollection<Student> = mongoDatabase.getCollection<Student>("students")

    suspend fun getAllStudents(): List<Student> = collection.find().toList()

    suspend fun getStudent(uuid: UUID) = collection.find(Filters.eq("_id", uuid)).firstOrNull()

    suspend fun createStudent(student: StudentCreateRequest) {
        val student =
            Student(
                name = student.name,
                studentId = UUID.randomUUID(),
                `class` = student.`class`,
                school = student.school,
            )
        collection.insertOne(student)
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
