package dev.crayson.smartgrades.services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.crayson.smartgrades.database.mongoDatabase
import dev.crayson.smartgrades.models.Student
import dev.crayson.smartgrades.models.StudentCreateRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.UUID

object StudentService {
    private val collection: MongoCollection<Student> = mongoDatabase.getCollection<Student>("students")

    suspend fun getAllStudents(): List<Student> = collection.find().toList()

    suspend fun getStudent(uuid: UUID) = collection.find(Filters.eq("_id", uuid.toString())).firstOrNull()

    suspend fun createStudent(student: StudentCreateRequest) {
        val student = Student(
            name = student.name,
            id = UUID.randomUUID().toString(),
            `class` = student.`class`,
            school = student.school,
        )
        collection.insertOne(student)
    }

    suspend fun updateStudent(student: Student): Boolean {
        val result =
            collection.replaceOne(
                Filters.eq("_id", student.id),
                student,
                ReplaceOptions().upsert(true),
            )
        return result.modifiedCount > 0 || result.upsertedId != null
    }

    suspend fun deleteStudent(uuid: UUID): Boolean {
        val result = collection.deleteOne(Filters.eq("_id", uuid.toString()))
        return result.deletedCount > 0
    }
}
