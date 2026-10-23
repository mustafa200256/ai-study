package iq.rafeeq.studentai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.GoldGradientCard
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.components.SectionHeader
import iq.rafeeq.studentai.ui.theme.*

data class BadgeItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean,
    val color: Color
)

@Composable
fun AchievementsScreen(
    studyRepository: StudyRepository,
    onNavigateBack: () -> Unit
) {
    val totalTasks by studyRepository.totalTasksCount.collectAsState(initial = 0)
    val completedTasks by studyRepository.completedTasksCount.collectAsState(initial = 0)
    val totalAttempts by studyRepository.totalQuizAttempts.collectAsState(initial = 0)
    val avgScore by studyRepository.averageQuizScore.collectAsState(initial = 0.0)

    val badges = listOf(
        BadgeItem(
            title = "أول خطوة في الطريق",
            description = "أكملت أول مهمة دراسية في جدولك",
            icon = Icons.Default.Flag,
            isUnlocked = completedTasks >= 1,
            color = GoldPrimary
        ),
        BadgeItem(
            title = "طالب مثابر",
            description = "أكملت 5 مهام دراسية بنجاح",
            icon = Icons.Default.WorkspacePremium,
            isUnlocked = completedTasks >= 5,
            color = WarningOrange
        ),
        BadgeItem(
            title = "المستكشف الوزاري",
            description = "أتممت أول اختبار تجريبي في المنهج",
            icon = Icons.Default.Quiz,
            isUnlocked = totalAttempts >= 1,
            color = InfoBlue
        ),
        BadgeItem(
            title = "علامة كاملة",
            description = "حققت نسبة 90% فما فوق في أحد الاختبارات",
            icon = Icons.Default.Grade,
            isUnlocked = (avgScore ?: 0.0) >= 90.0 && totalAttempts > 0,
            color = SuccessGreen
        ),
        BadgeItem(
            title = "عبقري المنهج",
            description = "أنجزت 10 مهام أو أكثر في وقت قياسي",
            icon = Icons.Default.Psychology,
            isUnlocked = completedTasks >= 10,
            color = Caramel
        ),
        BadgeItem(
            title = "بطل الـ 100",
            description = "أكملت 5 اختبارات بمعدل أعلى من 80%",
            icon = Icons.Default.MilitaryTech,
            isUnlocked = totalAttempts >= 5 && (avgScore ?: 0.0) >= 80.0,
            color = GoldWarm
        )
    )

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "الإنجازات والتقدم الدراسي",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Hero Stats Card
            item {
                GoldGradientCard {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(GoldPrimary.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = GoldWarm,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "لوحة وسام التفوق العراقي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "إحصائيات مستخرجة من دراستك الحقيقية",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CaramelLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatBox(title = "المهام المنجزة", value = "$completedTasks")
                            StatBox(title = "الاختبارات", value = "$totalAttempts")
                            StatBox(title = "متوسط الدرجات", value = "${avgScore?.toInt() ?: 0}%")
                        }
                    }
                }
            }

            item {
                SectionHeader(title = "أوسمة وشارات الإنجاز")
            }

            items(badges) { badge ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (badge.isUnlocked) MaterialTheme.colorScheme.surface else Navy700.copy(alpha = 0.6f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    if (badge.isUnlocked) badge.color.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.3f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = badge.icon,
                                contentDescription = null,
                                tint = if (badge.isUnlocked) badge.color else Color.Gray,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = badge.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = badge.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                            )
                        }

                        if (badge.isUnlocked) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "تم الفتح",
                                tint = SuccessGreen
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "مغلق",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = GoldWarm
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = BeigeSoft
        )
    }
}
