package iq.rafeeq.studentai.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import iq.rafeeq.studentai.data.remote.AiRemoteClient
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.navigation.Screen
import iq.rafeeq.studentai.ui.theme.*

@Composable
fun SettingsScreen(
    aiClient: AiRemoteClient,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onNavigate: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("rafeeq_settings", Context.MODE_PRIVATE) }
    var studentName by remember { mutableStateOf(prefs.getString("student_name", "طالب متميز") ?: "طالب متميز") }
    var backendUrl by remember { mutableStateOf(aiClient.getBackendUrl()) }
    var showUrlDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "الإعدادات العامة",
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            // Student Profile Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "بيانات الطالب الدراسية",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = studentName,
                            onValueChange = {
                                studentName = it
                                prefs.edit().putString("student_name", it).apply()
                            },
                            label = { Text("اسم الطالب أو اللقب") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "المرحلة الحالية: السادس الإعدادي (العلمي الأحيائي والتطبيقي)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Appearance Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "المظهر والشاشة",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "الوضع الليلي الكحلي الفاخر",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isDarkTheme) "الوضع الداكن مفعّل لراحة العين أثناء المذاكرة" else "الوضع الفاتح مفعّل",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = onThemeToggle,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = GoldPrimary,
                                    checkedTrackColor = Navy600
                                )
                            )
                        }
                    }
                }
            }

            // AI Server Configuration
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "خادم الذكاء الاصطناعي Backend",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "لحماية مفاتيح API ومنع تسريبها، يتصل التطبيق بخادم وسيط آمن.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Navy700, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "رابط الخادم الحالي:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Caramel
                                )
                                Text(
                                    text = backendUrl,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                            TextButton(onClick = { showUrlDialog = true }) {
                                Text(text = "تغيير", color = GoldPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // About & Legal
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigate(Screen.About.route) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = GoldPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "حول التطبيق والمبرمج", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = "Mustafa Hussein — مصطفى حسين", fontSize = 12.sp, color = Caramel)
                            }
                            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        HorizontalDivider(color = Navy600)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showPrivacyDialog = true }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = InfoBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "سياسة الخصوصية والأمان",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "رفيق الطالب العراقي AI • الإصدار 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }

    if (showUrlDialog) {
        var tempUrl by remember { mutableStateOf(backendUrl) }
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            title = { Text("تعديل رابط خادم الذكاء الاصطناعي", color = GoldPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "أدخل رابط خادم الـ Backend (أو Supabase Edge Function) الذي قمت بنشره:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = tempUrl,
                        onValueChange = { tempUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        aiClient.setBackendUrl(tempUrl.trim())
                        backendUrl = tempUrl.trim()
                        showUrlDialog = false
                        Toast.makeText(context, "تم حفظ رابط الخادم", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("حفظ", color = Navy900, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("إلغاء", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = Navy700
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("سياسة الخصوصية والأمان", color = GoldPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "نحن نحترم خصوصية الطالب العراقي تماماً:\n\n" +
                                "1. تحفظ بياناتك الدراسية وملاحظاتك واختباراتك محلياً على جهازك بواسطة قاعدة بيانات Room المشفرة.\n" +
                                "2. لا يتم طلب أي أذونات غير ضرورية مثل الكاميرا أو جهات الاتصال.\n" +
                                "3. أسئلة المدرس الذكي ترسل فقط إلى خادم Backend الآمن دون إرسال أي بيانات شخصية أو حساسة.\n" +
                                "4. لا نقوم بمشاركة أي بيانات مع أطراف ثالثة أو بيعها تجارياً.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BeigeSoft,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("موافق", color = Navy900, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Navy700
        )
    }
}
