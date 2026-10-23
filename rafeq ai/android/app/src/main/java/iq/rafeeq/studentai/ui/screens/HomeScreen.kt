package iq.rafeeq.studentai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.data.local.entity.StudyTaskEntity
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.GoldGradientCard
import iq.rafeeq.studentai.ui.components.SectionHeader
import iq.rafeeq.studentai.ui.navigation.Screen
import iq.rafeeq.studentai.ui.theme.*

@Composable
fun HomeScreen(
    studyRepository: StudyRepository,
    onNavigate: (String) -> Unit
) {
    val tasks by studyRepository.allTasks.collectAsState(initial = emptyList())
    val totalTasks by studyRepository.totalTasksCount.collectAsState(initial = 0)
    val completedTasks by studyRepository.completedTasksCount.collectAsState(initial = 0)
    val totalAttempts by studyRepository.totalQuizAttempts.collectAsState(initial = 0)
    val avgScore by studyRepository.averageQuizScore.collectAsState(initial = 0.0)

    val completionRate = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) else 0f
    val upcomingTasks = tasks.filter { !it.isCompleted }.take(3)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "أهلاً بك يا بطل 🎓",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "السادس الإعدادي — طريقك نحو الـ 100",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Caramel
                    )
                }
                Row {
                    IconButton(onClick = { onNavigate(Screen.Subscription.route) }) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "الاشتراك",
                            tint = GoldPrimary
                        )
                    }
                    IconButton(onClick = { onNavigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "الإعدادات",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // AI Tutor Card (Hero Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.AITutor.route) },
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Navy600, Navy700, Color(0xFF1E1B4B))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(GoldPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = GoldWarm,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "المساعد الذكي AI",
                                        color = GoldWarm,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "ابدأ المحادثة",
                                tint = GoldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "هل لديك سؤال صعب في الرياضيات أو الفيزياء؟",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "المدرس الذكي يشرح لك القوانين والمسائل خطوة بخطوة بالمنهج العراقي مع أمثلة وزارية.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BeigeSoft
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onNavigate(Screen.AITutor.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "اسأل المدرس الآن",
                                color = Navy900,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Study Progress Summary
        item {
            GoldGradientCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مستوى الإنجاز اليومي",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${(completionRate * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldWarm
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { completionRate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = GoldPrimary,
                        trackColor = Navy800
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(
                            label = "المهام المنجزة",
                            value = "$completedTasks / $totalTasks"
                        )
                        StatItem(
                            label = "الاختبارات",
                            value = "$totalAttempts"
                        )
                        StatItem(
                            label = "معدل الدرجات",
                            value = "${avgScore?.toInt() ?: 0}%"
                        )
                    }
                }
            }
        }

        // Quick Action Shortcuts Grid
        item {
            SectionHeader(title = "الأقسام السريعة")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickTile(
                    modifier = Modifier.weight(1f),
                    title = "المناهج والكتب",
                    icon = Icons.Default.MenuBook,
                    accentColor = GoldPrimary,
                    onClick = { onNavigate(Screen.Curriculum.route) }
                )
                QuickTile(
                    modifier = Modifier.weight(1f),
                    title = "الاختبارات الوزارية",
                    icon = Icons.Default.Quiz,
                    accentColor = Caramel,
                    onClick = { onNavigate(Screen.Quizzes.route) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickTile(
                    modifier = Modifier.weight(1f),
                    title = "جدول المذاكرة",
                    icon = Icons.Default.EventNote,
                    accentColor = WarningOrange,
                    onClick = { onNavigate(Screen.Schedule.route) }
                )
                QuickTile(
                    modifier = Modifier.weight(1f),
                    title = "الملاحظات والملخصات",
                    icon = Icons.Default.NoteAlt,
                    accentColor = InfoBlue,
                    onClick = { onNavigate(Screen.Notes.route) }
                )
            }
        }

        // Upcoming Tasks Section
        item {
            SectionHeader(
                title = "المهام القادمة",
                actionText = "عرض الكل",
                onActionClick = { onNavigate(Screen.Schedule.route) }
            )
        }

        if (upcomingTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "لا توجد مهام معلقة، أنت على أتم الاستعداد!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(upcomingTasks) { task ->
                TaskPreviewCard(
                    task = task,
                    onToggle = { completed ->
                        // Quick toggle
                    }
                )
            }
        }

        // Developer Attribution in Home Footer
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.About.route) }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "رفيق الطالب العراقي AI — تطوير: مصطفى حسين",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = CaramelLight
        )
    }
}

@Composable
private fun QuickTile(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TaskPreviewCard(
    task: StudyTaskEntity,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            when (task.priority) {
                                "عالية" -> ErrorRed
                                "متوسطة" -> WarningOrange
                                else -> InfoBlue
                            },
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${task.subject} • موعد: ${task.dueDate} ${task.dueTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
