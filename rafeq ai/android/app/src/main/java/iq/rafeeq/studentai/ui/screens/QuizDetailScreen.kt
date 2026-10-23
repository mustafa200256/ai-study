package iq.rafeeq.studentai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.data.local.entity.QuizAttemptEntity
import iq.rafeeq.studentai.data.local.entity.QuizQuestionEntity
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun QuizDetailScreen(
    subjectId: String,
    studyRepository: StudyRepository,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var questions by remember { mutableStateOf<List<QuizQuestionEntity>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(subjectId) {
        questions = studyRepository.getQuestionsBySubject(subjectId)
    }

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "اختبار المنهج الوزاري",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldPrimary)
            }
        } else if (quizCompleted) {
            // Result Screen
            val total = questions.size
            val percentage = if (total > 0) ((correctCount.toFloat() / total) * 100).toInt() else 0

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (percentage >= 70) Icons.Default.EmojiEvents else Icons.Default.Info,
                    contentDescription = null,
                    tint = if (percentage >= 70) GoldPrimary else WarningOrange,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (percentage >= 70) "أحسنت! إنجاز رائع 🌟" else "بداية جيدة وتحتاج مراجعة 📚",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "درجتك النهائية في الاختبار:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (percentage >= 70) SuccessGreen else WarningOrange
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$correctCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            Text(text = "صحيحة", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$wrongCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ErrorRed)
                            Text(text = "خاطئة", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = GoldWarm)
                            Text(text = "إجمالي الأسئلة", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        // Reset Quiz
                        currentIndex = 0
                        selectedOptionIndex = null
                        isSubmitted = false
                        correctCount = 0
                        wrongCount = 0
                        quizCompleted = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = "إعادة الاختبار من جديد", color = Navy900, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = "العودة لقائمة الاختبارات", color = GoldWarm)
                }
            }
        } else {
            // Active Question
            val currentQuestion = questions[currentIndex]
            val total = questions.size
            val options = listOf(
                currentQuestion.optionA,
                currentQuestion.optionB,
                currentQuestion.optionC,
                currentQuestion.optionD
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "السؤال ${currentIndex + 1} من $total",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GoldWarm
                    )
                    Text(
                        text = currentQuestion.subjectName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / total },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GoldPrimary,
                    trackColor = Navy800
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Question Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 26.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                options.forEachIndexed { optIndex, optionText ->
                    val isSelected = selectedOptionIndex == optIndex
                    val isCorrect = optIndex == currentQuestion.correctIndex

                    val borderColor = when {
                        !isSubmitted && isSelected -> GoldPrimary
                        isSubmitted && isCorrect -> SuccessGreen
                        isSubmitted && isSelected && !isCorrect -> ErrorRed
                        else -> Navy600
                    }

                    val bgColor = when {
                        !isSubmitted && isSelected -> GoldPrimary.copy(alpha = 0.15f)
                        isSubmitted && isCorrect -> SuccessGreen.copy(alpha = 0.2f)
                        isSubmitted && isSelected && !isCorrect -> ErrorRed.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(enabled = !isSubmitted) {
                                selectedOptionIndex = optIndex
                            },
                        colors = CardDefaults.cardColors(containerColor = bgColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (optIndex) {
                                    0 -> "أ"
                                    1 -> "ب"
                                    2 -> "ج"
                                    else -> "د"
                                },
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                modifier = Modifier.width(28.dp)
                            )
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Explanation Banner
                if (isSubmitted) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy700)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GoldWarm,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "توضيح الإجابة النموذجية:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldWarm
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = currentQuestion.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = BeigeSoft,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Button (تحقق / التالي)
                if (!isSubmitted) {
                    Button(
                        onClick = {
                            selectedOptionIndex?.let { sel ->
                                isSubmitted = true
                                if (sel == currentQuestion.correctIndex) {
                                    correctCount++
                                } else {
                                    wrongCount++
                                }
                            }
                        },
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "تأكيد الإجابة والتحقق",
                            color = Navy900,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < total - 1) {
                                currentIndex++
                                selectedOptionIndex = null
                                isSubmitted = false
                            } else {
                                // Save attempt to DB
                                val score = ((correctCount.toFloat() / total) * 100).toInt()
                                coroutineScope.launch {
                                    studyRepository.recordQuizAttempt(
                                        QuizAttemptEntity(
                                            subjectName = currentQuestion.subjectName,
                                            totalQuestions = total,
                                            correctAnswers = correctCount,
                                            wrongAnswers = wrongCount,
                                            scorePercentage = score
                                        )
                                    )
                                }
                                quizCompleted = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (currentIndex < total - 1) "السؤال التالي" else "عرض النتيجة النهائية",
                            color = Navy900,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
