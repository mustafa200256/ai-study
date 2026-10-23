package iq.rafeeq.studentai.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.data.local.entity.StudyTaskEntity
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    studyRepository: StudyRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tasks by studyRepository.allTasks.collectAsState(initial = emptyList())
    val totalTasks by studyRepository.totalTasksCount.collectAsState(initial = 0)
    val completedTasks by studyRepository.completedTasksCount.collectAsState(initial = 0)

    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<StudyTaskEntity?>(null) }
    var selectedFilter by remember { mutableStateOf("الكل") } // "الكل", "المعلقة", "المكتملة"

    val completionRate = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) else 0f

    val filteredTasks = when (selectedFilter) {
        "المعلقة" -> tasks.filter { !it.isCompleted }
        "المكتملة" -> tasks.filter { it.isCompleted }
        else -> tasks
    }

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "جدول المذاكرة الذكي",
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    taskToEdit = null
                    showAddDialog = true
                },
                containerColor = GoldPrimary,
                contentColor = Navy900
            ) {
                Icon(Icons.Default.Add, contentDescription = "إضافة مهمة")
            }
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
            // Progress Bar Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "نسبة الإنجاز الكلي",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$completedTasks من أصل $totalTasks مهام",
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldWarm,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { completionRate },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GoldPrimary,
                            trackColor = Navy800
                        )
                    }
                }
            }

            // Filter Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("الكل", "المعلقة", "المكتملة").forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Navy900
                            )
                        )
                    }
                }
            }

            if (filteredTasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "لا توجد مهام دراسية في هذا القسم",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredTasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { isChecked ->
                            coroutineScope.launch {
                                studyRepository.toggleTask(task.id, isChecked)
                            }
                        },
                        onEdit = {
                            taskToEdit = task
                            showAddDialog = true
                        },
                        onDelete = {
                            coroutineScope.launch {
                                studyRepository.deleteTask(task)
                                Toast.makeText(context, "تم حذف المهمة", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        TaskDialog(
            initialTask = taskToEdit,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, subject, date, time, priority ->
                coroutineScope.launch {
                    if (taskToEdit == null) {
                        studyRepository.addTask(
                            StudyTaskEntity(
                                title = title,
                                subject = subject,
                                dueDate = date,
                                dueTime = time,
                                priority = priority
                            )
                        )
                        Toast.makeText(context, "تمت إضافة المهمة بنجاح", Toast.LENGTH_SHORT).show()
                    } else {
                        studyRepository.updateTask(
                            taskToEdit!!.copy(
                                title = title,
                                subject = subject,
                                dueDate = date,
                                dueTime = time,
                                priority = priority
                            )
                        )
                        Toast.makeText(context, "تم تعديل المهمة بنجاح", Toast.LENGTH_SHORT).show()
                    }
                    showAddDialog = false
                }
            }
        )
    }
}

@Composable
fun TaskCard(
    task: StudyTaskEntity,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = GoldPrimary,
                    checkmarkColor = Navy900
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.subject,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldWarm
                    )
                    Text(
                        text = " • ${task.dueDate} ${task.dueTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Priority Badge
            Box(
                modifier = Modifier
                    .background(
                        when (task.priority) {
                            "عالية" -> ErrorRed.copy(alpha = 0.2f)
                            "متوسطة" -> WarningOrange.copy(alpha = 0.2f)
                            else -> InfoBlue.copy(alpha = 0.2f)
                        },
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = task.priority,
                    style = MaterialTheme.typography.labelSmall,
                    color = when (task.priority) {
                        "عالية" -> ErrorRed
                        "متوسطة" -> WarningOrange
                        else -> InfoBlue
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "تعديل",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = ErrorRed.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun TaskDialog(
    initialTask: StudyTaskEntity?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, subject: String, date: String, time: String, priority: String) -> Unit
) {
    var title by remember { mutableStateOf(initialTask?.title ?: "") }
    var subject by remember { mutableStateOf(initialTask?.subject ?: "الرياضيات") }
    var date by remember { mutableStateOf(initialTask?.dueDate ?: "اليوم") }
    var time by remember { mutableStateOf(initialTask?.dueTime ?: "18:00") }
    var priority by remember { mutableStateOf(initialTask?.priority ?: "متوسطة") }

    val subjects = listOf("الرياضيات", "الفيزياء", "الكيمياء", "الأحياء", "اللغة العربية", "اللغة الإنجليزية", "الإسلامية")
    val priorities = listOf("عالية", "متوسطة", "منخفضة")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialTask == null) "إضافة مهمة دراسية جديدة" else "تعديل المهمة الدراسية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان المهمة أو الدرس") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("المادة الدراسية:", style = MaterialTheme.typography.labelSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    subjects.take(4).forEach { subj ->
                        FilterChip(
                            selected = subject == subj,
                            onClick = { subject = subj },
                            label = { Text(subj, fontSize = 11.sp) }
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("التاريخ") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("الوقت") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("درجة الأهمية والأولوية:", style = MaterialTheme.typography.labelSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    priorities.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p, fontSize = 12.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), subject, date, time, priority)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Text(
                    text = if (initialTask == null) "إضافة" else "حفظ التعديلات",
                    color = Navy900,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        containerColor = Navy700
    )
}
