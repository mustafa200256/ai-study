package iq.rafeeq.studentai.data.repository

import iq.rafeeq.studentai.data.local.AppDatabase
import iq.rafeeq.studentai.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val db: AppDatabase) {

    // Tasks
    val allTasks: Flow<List<StudyTaskEntity>> = db.taskDao().getAllTasks()
    val totalTasksCount: Flow<Int> = db.taskDao().getTotalTasksCount()
    val completedTasksCount: Flow<Int> = db.taskDao().getCompletedTasksCount()

    suspend fun addTask(task: StudyTaskEntity): Long = db.taskDao().insertTask(task)
    suspend fun updateTask(task: StudyTaskEntity) = db.taskDao().updateTask(task)
    suspend fun deleteTask(task: StudyTaskEntity) = db.taskDao().deleteTask(task)
    suspend fun toggleTask(id: Long, completed: Boolean) = db.taskDao().toggleTaskCompletion(id, completed)

    // Notes
    val allNotes: Flow<List<NoteEntity>> = db.noteDao().getAllNotes()
    fun searchNotes(query: String): Flow<List<NoteEntity>> = db.noteDao().searchNotes(query)
    suspend fun addNote(note: NoteEntity): Long = db.noteDao().insertNote(note)
    suspend fun updateNote(note: NoteEntity) = db.noteDao().updateNote(note)
    suspend fun deleteNote(note: NoteEntity) = db.noteDao().deleteNote(note)

    // Quizzes
    suspend fun getQuestionsBySubject(subjectId: String): List<QuizQuestionEntity> =
        db.quizDao().getQuestionsBySubject(subjectId)

    val allQuizAttempts: Flow<List<QuizAttemptEntity>> = db.quizDao().getAllAttempts()
    val totalQuizAttempts: Flow<Int> = db.quizDao().getTotalAttemptsCount()
    val averageQuizScore: Flow<Double?> = db.quizDao().getAverageScore()

    suspend fun recordQuizAttempt(attempt: QuizAttemptEntity): Long =
        db.quizDao().insertAttempt(attempt)

    // Payments
    val allPayments: Flow<List<PaymentRequestEntity>> = db.paymentDao().getAllPayments()
    suspend fun submitPaymentRequest(request: PaymentRequestEntity): Long =
        db.paymentDao().insertPayment(request)
}
