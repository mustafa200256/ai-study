package iq.rafeeq.studentai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.ui.components.GoldGradientCard
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.theme.*

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "حول التطبيق والمطور",
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
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            // Hero Card - Developer & Owner Attribution
            item {
                GoldGradientCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(GoldPrimary.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = GoldWarm,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "رفيق الطالب العراقي AI",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Rafeeq Student AI • الإصدار 1.0.0",
                            style = MaterialTheme.typography.bodySmall,
                            color = CaramelLight
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Navy800.copy(alpha = 0.8f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "صاحب المشروع والمبرمج الرئيسي:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Caramel
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Mustafa Hussein — مصطفى حسين",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Project Vision Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Stars, contentDescription = null, tint = GoldWarm)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "رسالة ورؤية المشروع",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "مشروع «رفيق الطالب العراقي AI» هو منصة تعليمية متكاملة موجهة لطلبة العراق، خصوصاً مرحلة السادس الإعدادي والصفوف المنتهية. يهدف التطبيق إلى تمكين الطالب من فهم المناهج الصعبة خطوة بخطوة بالاستفادة من أحدث تقنيات الذكاء الاصطناعي وبنك الأسئلة الوزارية وجدول الدراسة الذكي.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Technology Stack
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = InfoBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "التقنيات المستخدمة في البناء",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val techList = listOf(
                            "Kotlin Native مع Jetpack Compose و Material 3",
                            "قاعدة بيانات محلية سريعة Room Database مع دعم كامل للعمل بدون إنترنت",
                            "واجهات بتصميم كحلي وذهبي مع دعم اتجاه RTL للغة العربية الأصيلة",
                            "طبقة Backend وسيطة آمنة تدعم نماذج الذكاء الاصطناعي (Groq LLaMA 3.3) دون تسريب المفاتيح",
                            "لوحة تحكم سحابية لإدارة الطلاب، المحتوى، وطلبات دفع المحافظ"
                        )

                        techList.forEach { tech ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "•", color = GoldPrimary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tech,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
