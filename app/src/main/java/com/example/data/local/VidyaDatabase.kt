package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "downloaded_lessons")
data class DownloadedLessonEntity(
    @PrimaryKey val lectureId: String,
    val classNumber: Int,
    val subjectEn: String,
    val subjectHi: String,
    val titleEn: String,
    val titleHi: String,
    val cachedSummaryEn: String,
    val cachedSummaryHi: String,
    val cachedFormulas: String,
    val diagramTypeName: String,
    val downloadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_diagram_notes")
data class SavedDiagramNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lectureId: String,
    val title: String,
    val classNumber: Int,
    val subject: String,
    val noteContent: String,
    val diagramTypeName: String,
    val strokesSerialized: String, // Serialized strokes for re-opening on Canvas
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_attempts")
data class TestAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testId: String,
    val testTitle: String,
    val classNumber: Int,
    val subject: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val attemptedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_tasks")
data class StudyTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayLabel: String,
    val classNumber: Int,
    val subject: String,
    val topicEn: String,
    val topicHi: String,
    val durationMinutes: Int,
    val isCompleted: Boolean = false,
    val xpReward: Int = 30
)

@Dao
interface VidyaDao {
    // Offline Downloaded Lessons
    @Query("SELECT * FROM downloaded_lessons ORDER BY downloadedAt DESC")
    fun getAllDownloadedLessons(): Flow<List<DownloadedLessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadedLesson(lesson: DownloadedLessonEntity)

    @Query("DELETE FROM downloaded_lessons WHERE lectureId = :lectureId")
    suspend fun deleteDownloadedLesson(lectureId: String)

    // Saved Diagram & AI Notes
    @Query("SELECT * FROM saved_diagram_notes ORDER BY createdAt DESC")
    fun getAllSavedNotes(): Flow<List<SavedDiagramNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedNote(note: SavedDiagramNoteEntity)

    @Query("DELETE FROM saved_diagram_notes WHERE id = :id")
    suspend fun deleteSavedNote(id: Int)

    // Mock Test Attempts
    @Query("SELECT * FROM test_attempts ORDER BY attemptedAt DESC")
    fun getAllTestAttempts(): Flow<List<TestAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestAttempt(attempt: TestAttemptEntity)

    // Study Plan Tasks
    @Query("SELECT * FROM study_tasks ORDER BY id ASC")
    fun getAllStudyTasks(): Flow<List<StudyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyTask(task: StudyTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyTasks(tasks: List<StudyTaskEntity>)

    @Query("UPDATE study_tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, completed: Boolean)

    @Query("DELETE FROM study_tasks")
    suspend fun clearAllStudyTasks()
}

@Database(
    entities = [
        DownloadedLessonEntity::class,
        SavedDiagramNoteEntity::class,
        TestAttemptEntity::class,
        StudyTaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VidyaDatabase : RoomDatabase() {
    abstract fun vidyaDao(): VidyaDao

    companion object {
        @Volatile
        private var INSTANCE: VidyaDatabase? = null

        fun getDatabase(context: Context): VidyaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VidyaDatabase::class.java,
                    "vidyasetu_offline.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class VidyaRepository(private val dao: VidyaDao) {
    val downloadedLessons: Flow<List<DownloadedLessonEntity>> = dao.getAllDownloadedLessons()
    val savedNotes: Flow<List<SavedDiagramNoteEntity>> = dao.getAllSavedNotes()
    val testAttempts: Flow<List<TestAttemptEntity>> = dao.getAllTestAttempts()
    val studyTasks: Flow<List<StudyTaskEntity>> = dao.getAllStudyTasks()

    suspend fun saveOfflineLesson(lesson: DownloadedLessonEntity) = dao.insertDownloadedLesson(lesson)
    suspend fun removeOfflineLesson(lectureId: String) = dao.deleteDownloadedLesson(lectureId)

    suspend fun saveDiagramNote(note: SavedDiagramNoteEntity) = dao.insertSavedNote(note)
    suspend fun deleteDiagramNote(id: Int) = dao.deleteSavedNote(id)

    suspend fun recordTestAttempt(attempt: TestAttemptEntity) = dao.insertTestAttempt(attempt)

    suspend fun replaceStudyPlan(tasks: List<StudyTaskEntity>) {
        dao.clearAllStudyTasks()
        dao.insertStudyTasks(tasks)
    }

    suspend fun addStudyTask(task: StudyTaskEntity) = dao.insertStudyTask(task)

    suspend fun setStudyTaskCompleted(taskId: Int, completed: Boolean) =
        dao.updateTaskCompletion(taskId, completed)
}
