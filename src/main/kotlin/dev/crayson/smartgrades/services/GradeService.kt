package dev.crayson.smartgrades.services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.crayson.smartgrades.database.mongoDatabase
import dev.crayson.smartgrades.models.dto.grade.GradeCreateRequest
import dev.crayson.smartgrades.models.entity.Grade
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.UUID

object GradeService {
    private val collection: MongoCollection<Grade> = mongoDatabase.getCollection<Grade>("grades")

    suspend fun getAllGrades(subjectId: UUID): List<Grade> = collection.find(Filters.eq("subjectId", subjectId)).toList()

    suspend fun getGrade(gradeId: UUID): Grade? = collection.find(Filters.eq("_id", gradeId)).firstOrNull()

    suspend fun createGrade(request: GradeCreateRequest): Grade {
        val grade =
            Grade(
                gradeId = UUID.randomUUID(),
                subjectId = request.subjectId,
                value = request.value,
                date = request.date,
                type = request.type,
            )
        collection.insertOne(grade)
        return grade
    }

    suspend fun updateGrade(grade: Grade): Boolean {
        val result =
            collection.replaceOne(
                Filters.eq("_id", grade.gradeId),
                grade,
                ReplaceOptions().upsert(false),
            )
        return result.modifiedCount > 0
    }

    suspend fun deleteGrade(gradeId: UUID): Boolean {
        val result = collection.deleteOne(Filters.eq("_id", gradeId))
        return result.deletedCount > 0
    }
}
