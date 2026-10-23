package iq.rafeeq.studentai.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import iq.rafeeq.studentai.data.local.entity.PaymentRequestEntity
import iq.rafeeq.studentai.data.repository.StudyRepository
import iq.rafeeq.studentai.ui.components.GoldGradientCard
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.components.SectionHeader
import iq.rafeeq.studentai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SubscriptionScreen(
    studyRepository: StudyRepository,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val paymentRequests by studyRepository.allPayments.collectAsState(initial = emptyList())

    var selectedPlan by remember { mutableStateOf("باقة التفوق الذهبية (ترم كامل)") }
    var selectedWallet by remember { mutableStateOf("محفظة SuperQi") }
    var senderPhone by remember { mutableStateOf("") }
    var transactionRef by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val walletNumber = "07701234567" // Can be managed from Admin Web Panel

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "الاشتراك وترقية الحساب",
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
            // Hero Plan Card
            item {
                GoldGradientCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "باقة رفيق AI المميزة 👑",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .background(GoldPrimary, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "10,000 د.ع",
                                    color = Navy900,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val features = listOf(
                            "محادثات ذكاء اصطناعي غير محدودة مع المدرس الذكي",
                            "شروحات تفصيلية لجميع وزاريات السادس من 2013 إلى 2026",
                            "بنك أسئلة مرشحات الوزاري وملاحظات خاصة",
                            "دعم فني مباشر وتواصل دراسي مستمر"
                        )

                        features.forEach { feat ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = GoldWarm,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = feat,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BeigeSoft
                                )
                            }
                        }
                    }
                }
            }

            // Wallet Transfer Instructions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "طريقة الدفع عبر المحافظ الإلكترونية العراقية:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "1. افتح تطبيق محفظتك (SuperQi أو زين كاش أو FIB).\n" +
                                    "2. قم بتحويل مبلغ الاشتراك (10,000 دينار عراقي) إلى الرقم أدناه.\n" +
                                    "3. انسخ رقم الإشعار/العملية واكتبه في النموذج أدناه للتأكيد.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Navy700, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "رقم المحفظة المعتمد:", style = MaterialTheme.typography.labelSmall, color = Caramel)
                                Text(text = walletNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Wallet Number", walletNumber)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ رقم المحفظة", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = GoldPrimary)
                            }
                        }
                    }
                }
            }

            // Submission Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "تأكيد عملية التحويل:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text("المحفظة المستخدمة:", style = MaterialTheme.typography.labelSmall)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("محفظة SuperQi", "زين كاش", "FIB").forEach { wallet ->
                                FilterChip(
                                    selected = selectedWallet == wallet,
                                    onClick = { selectedWallet = wallet },
                                    label = { Text(wallet, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GoldPrimary,
                                        selectedLabelColor = Navy900
                                    )
                                )
                            }
                        }

                        OutlinedTextField(
                            value = senderPhone,
                            onValueChange = { senderPhone = it },
                            label = { Text("رقم هاتفك المحول منه") },
                            placeholder = { Text("07XXXXXXXXX") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = transactionRef,
                            onValueChange = { transactionRef = it },
                            label = { Text("رقم المعاملة / رقم الإشعار") },
                            placeholder = { Text("مثال: TRX-9823412") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Disclaimer Note
                        Text(
                            text = "ملاحظة أمان: يتم تدقيق الإشعار ومطابقة المبلغ يدوياً من لوحة إدارة المشروع لضمان تفعيل الحساب بأمان.",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarningOrange
                        )

                        Button(
                            onClick = {
                                if (senderPhone.isNotBlank() && transactionRef.isNotBlank()) {
                                    isSubmitting = true
                                    coroutineScope.launch {
                                        studyRepository.submitPaymentRequest(
                                            PaymentRequestEntity(
                                                planName = selectedPlan,
                                                priceIqd = "10,000 د.ع",
                                                walletType = selectedWallet,
                                                senderPhone = senderPhone.trim(),
                                                transactionRef = transactionRef.trim(),
                                                status = "قيد المراجعة"
                                            )
                                        )
                                        senderPhone = ""
                                        transactionRef = ""
                                        isSubmitting = false
                                        Toast.makeText(context, "تم إرسال طلب التفعيل إلى الإدارة بنجاح", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(context, "يرجى كتابة رقم الهاتف ورقم المعاملة", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "إرسال طلب التفعيل للإدارة",
                                color = Navy900,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Payment Requests History
            if (paymentRequests.isNotEmpty()) {
                item {
                    SectionHeader(title = "طلبات التفعيل السابقة")
                }

                items(paymentRequests) { req ->
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
                                    text = "${req.planName} • ${req.walletType}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "رقم المعاملة: ${req.transactionRef}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CaramelLight
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        when (req.status) {
                                            "مقبول" -> SuccessGreen.copy(alpha = 0.2f)
                                            "مرفوض" -> ErrorRed.copy(alpha = 0.2f)
                                            else -> WarningOrange.copy(alpha = 0.2f)
                                        },
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = req.status,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = when (req.status) {
                                        "مقبول" -> SuccessGreen
                                        "مرفوض" -> ErrorRed
                                        else -> WarningOrange
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
