package iq.rafeeq.studentai.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.data.local.entity.NoteEntity
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    studyRepository: StudyRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val notes by if (searchQuery.isBlank()) {
        studyRepository.allNotes.collectAsState(initial = emptyList())
    } else {
        studyRepository.searchNotes(searchQuery).collectAsState(initial = emptyList())
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<NoteEntity?>(null) }

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "الملاحظات والملخصات",
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    noteToEdit = null
                    showAddDialog = true
                },
                containerColor = GoldPrimary,
                contentColor = Navy900
            ) {
                Icon(Icons.Default.Add, contentDescription = "تدوين ملاحظة")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("ابحث في الملاحظات والملخصات...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = Navy600
                ),
                singleLine = true
            )

            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد ملاحظات مسجلة بعد",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "اضغط على زر (+) لتدوين أول ملخص أو فائدة وزارية",
                            style = MaterialTheme.typography.bodySmall,
                            color = CaramelLight
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
                            onEdit = {
                                noteToEdit = note
                                showAddDialog = true
                            },
                            onDelete = {
                                coroutineScope.launch {
                                    studyRepository.deleteNote(note)
                                    Toast.makeText(context, "تم حذف الملاحظة", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(note.title, "${note.title}\n\n${note.content}")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ الملاحظة إلى الحافظة", Toast.LENGTH_SHORT).show()
                            },
                            onShare = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}\n\n— عبر تطبيق رفيق الطالب العراقي AI")
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "مشاركة الملاحظة")
                                context.startActivity(shareIntent)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        NoteDialog(
            initialNote = noteToEdit,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content, subject ->
                coroutineScope.launch {
                    if (noteToEdit == null) {
                        studyRepository.addNote(
                            NoteEntity(
                                title = title,
                                content = content,
                                subject = subject
                            )
                        )
                        Toast.makeText(context, "تم حفظ الملاحظة", Toast.LENGTH_SHORT).show()
                    } else {
                        studyRepository.updateNote(
                            noteToEdit!!.copy(
                                title = title,
                                content = content,
                                subject = subject,
                                updatedAt = System.currentTimeMillis()
                            )
                        )
                        Toast.makeText(context, "تم تحديث الملاحظة", Toast.LENGTH_SHORT).show()
                    }
                    showAddDialog = false
                }
            }
        )
    }
}

@Composable
fun NoteCard(
    note: NoteEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(GoldPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = note.subject,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldWarm,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "نسخ",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "تعديل",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
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
}

@Composable
fun NoteDialog(
    initialNote: NoteEntity?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, content: String, subject: String) -> Unit
) {
    var title by remember { mutableStateOf(initialNote?.title ?: "") }
    var content by remember { mutableStateOf(initialNote?.content ?: "") }
    var subject by remember { mutableStateOf(initialNote?.subject ?: "عام") }

    val subjects = listOf("عام", "الرياضيات", "الفيزياء", "الكيمياء", "الأحياء", "اللغة العربية", "اللغة الإنجليزية")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialNote == null) "تدوين ملاحظة جديدة" else "تعديل الملاحظة",
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
                    label = { Text("عنوان الملاحظة أو الدرس") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("المادة:", style = MaterialTheme.typography.labelSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    subjects.take(4).forEach { s ->
                        FilterChip(
                            selected = subject == s,
                            onClick = { subject = s },
                            label = { Text(s, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("نص الملاحظة أو التلخيص") },
                    minLines = 4,
                    maxLines = 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onConfirm(title.trim(), content.trim(), subject)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Text(
                    text = if (initialNote == null) "حفظ الملاحظة" else "حفظ التعديلات",
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
