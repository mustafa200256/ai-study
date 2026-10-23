package iq.rafeeq.studentai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_tasks")
data class StudyTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val dueDate: String,
    val dueTime: String,
    val priority: String, // "عالية", "متوسطة", "منخفضة"
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val subject: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: String,
    val subjectName: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    val explanation: String
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectName: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val scorePercentage: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isUser: Boolean,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isError: Boolean = false
)

@Entity(tableName = "payment_requests")
data class PaymentRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planName: String,
    val priceIqd: String,
    val walletType: String,
    val senderPhone: String,
    val transactionRef: String,
    val status: String = "قيد المراجعة", // "قيد المراجعة", "مقبول", "مرفوض"
    val createdAt: Long = System.currentTimeMillis()
)
