package iq.rafeeq.studentai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.data.local.entity.QuizAttemptEntity
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.components.SectionHeader
import iq.rafeeq.studentai.ui.navigation.Screen
import iq.rafeeq.studentai.ui.theme.*

data class QuizSubjectCardData(
    val id: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val questionsCount: String,
    val color: Color
)

@Composable
fun QuizzesScreen(
    studyRepository: StudyRepository,
    onNavigate: (String) -> Unit
) {
    val attempts by studyRepository.allQuizAttempts.collectAsState(initial = emptyList())
    val totalAttempts by studyRepository.totalQuizAttempts.collectAsState(initial = 0)
    val avgScore by studyRepository.averageQuizScore.collectAsState(initial = 0.0)

    val quizSubjects = listOf(
        QuizSubjectCardData("math", "اختبار الرياضيات (السادس العلمي)", Icons.Default.Calculate, "أسئلة وزارية ونموذجية", GoldPrimary),
        QuizSubjectCardData("physics", "اختبار الفيزياء (الفصل 1 و 2)", Icons.Default.Bolt, "المتسعات والحث الكهرومغناطيسي", WarningOrange),
        QuizSubjectCardData("chemistry", "اختبار الكيمياء (الثرموداينمك وبفر)", Icons.Default.Science, "قوانين كبس والاتزان", SuccessGreen),
        QuizSubjectCardData("arabic", "اختبار قواعد اللغة العربية (الاستفهام والنفي)", Icons.Default.AutoStories, "إعراب ومعاني الأدوات", Caramel)
    )

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "الاختبارات التفاعلية والوزاريات",
                canNavigateBack = false
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Stats summary card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$totalAttempts",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = "محاولات مكتملة",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        VerticalDivider(modifier = Modifier.height(36.dp), color = Navy600)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${avgScore?.toInt() ?: 0}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Text(
                                text = "معدل الإجابات الصحيحة",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                SectionHeader(title = "اختر المادة لبدء الاختبار")
            }

            items(quizSubjects) { subject ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigate(Screen.QuizDetail.createRoute(subject.id)) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(subject.color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = subject.icon,
                                contentDescription = null,
                                tint = subject.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = subject.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = subject.questionsCount,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { onNavigate(Screen.QuizDetail.createRoute(subject.id)) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "ابدأ",
                                color = Navy900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Attempt History
            if (attempts.isNotEmpty()) {
                item {
                    SectionHeader(title = "سجل الاختبارات السابقة")
                }

                items(attempts) { attempt ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy700)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = attempt.subjectName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${attempt.correctAnswers} صحيحة من أصل ${attempt.totalQuestions}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BeigeSoft
                                )
                            }

                            Text(
                                text = "${attempt.scorePercentage}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (attempt.scorePercentage >= 70) SuccessGreen else WarningOrange
                            )
                        }
                    }
                }
            }
        }
    }
}
