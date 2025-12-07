package dev.crayson.smartgrades.services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoCollection
import dev.crayson.smartgrades.database.mongoDatabase
import dev.crayson.smartgrades.models.dto.subject.SubjectCreateRequest
import dev.crayson.smartgrades.models.entity.Subject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.UUID

object SubjectService {
    private val collection: MongoCollection<Subject> =
        mongoDatabase.getCollection<Subject>("subjects")

    suspend fun getAllSubjects(studentId: UUID): List<Subject> =
        collection
            .find(Filters.eq("studentId", studentId.toString()))
            .toList()

    suspend fun getSubject(subjectId: UUID): Subject? =
        collection
            .find(Filters.eq("_id", subjectId.toString()))
            .firstOrNull()

    suspend fun createSubject(request: SubjectCreateRequest) {
        val subject =
            Subject(
                subjectId = UUID.randomUUID().toString(),
                studentId = request.studentId,
                name = request.name,
                subjectTyp = request.subjectType,
            )

        collection.insertOne(subject)
    }

    suspend fun updateSubject(subject: Subject): Boolean {
        val result =
            collection.replaceOne(
                Filters.eq("_id", subject.subjectId),
                subject,
                ReplaceOptions().upsert(true),
            )

        return result.modifiedCount > 0
    }

    suspend fun deleteSubject(subjectId: UUID): Boolean {
        val result =
            collection.deleteOne(Filters.eq("_id", subjectId.toString()))

        return result.deletedCount > 0
    }
}
