package iq.rafeeq.studentai.data.local.dao

import androidx.room.*
import iq.rafeeq.studentai.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM study_tasks ORDER BY isCompleted ASC, dueDate ASC, dueTime ASC")
    fun getAllTasks(): Flow<List<StudyTaskEntity>>

    @Query("SELECT COUNT(*) FROM study_tasks")
    fun getTotalTasksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM study_tasks WHERE isCompleted = 1")
    fun getCompletedTasksCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTaskEntity): Long

    @Update
    suspend fun updateTask(task: StudyTaskEntity)

    @Delete
    suspend fun deleteTask(task: StudyTaskEntity)

    @Query("UPDATE study_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun toggleTaskCompletion(id: Long, completed: Boolean)
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions WHERE subjectId = :subjectId")
    suspend fun getQuestionsBySubject(subjectId: String): List<QuizQuestionEntity>

    @Query("SELECT COUNT(*) FROM quiz_questions")
    suspend fun getQuestionsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuizAttemptEntity>>

    @Query("SELECT COUNT(*) FROM quiz_attempts")
    fun getTotalAttemptsCount(): Flow<Int>

    @Query("SELECT AVG(scorePercentage) FROM quiz_attempts")
    fun getAverageScore(): Flow<Double?>

    @Insert
    suspend fun insertAttempt(attempt: QuizAttemptEntity): Long
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert
    suspend fun insertMessage(msg: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payment_requests ORDER BY createdAt DESC")
    fun getAllPayments(): Flow<List<PaymentRequestEntity>>

    @Insert
    suspend fun insertPayment(req: PaymentRequestEntity): Long

    @Query("UPDATE payment_requests SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}
