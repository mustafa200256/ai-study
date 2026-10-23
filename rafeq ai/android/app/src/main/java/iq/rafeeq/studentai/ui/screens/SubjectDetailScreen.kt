package iq.rafeeq.studentai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.navigation.Screen
import iq.rafeeq.studentai.ui.theme.*

data class ChapterDetail(
    val title: String,
    val topics: String,
    val ministerialImportance: String // e.g. "20-25 درجة وزاري"
)

@Composable
fun SubjectDetailScreen(
    subjectId: String,
    onNavigateBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val (subjectName, chapters) = when (subjectId) {
        "math" -> Pair(
            "الرياضيات — السادس العلمي",
            listOf(
                ChapterDetail("الفصل الأول: الأعداد المركبة", "العمليات الحسابية، الصيغة القطبية، مبرهنة ديموافر والجذور النونية", "20 درجة وزاري"),
                ChapterDetail("الفصل الثاني: القطوع المخروطية", "القطع المكافئ، القطع الناقص، القطع الزائد وإيجاد المعادلات المشتركة", "20 درجة وزاري"),
                ChapterDetail("الفصل الثالث: تطبيقات التفاضل", "المعدلات الزمنية، مبرهنتا رول والقيمة المتوسطة، التقريب، رسم الدوال، التطبيقات العملية", "30-40 درجة وزاري"),
                ChapterDetail("الفصل الرابع: التكامل", "التكامل غير المحدد، التكامل المحدد، الدوال المثلثية واللوغارتمية، المساحات والحجوم الدورانية", "30 درجة وزاري"),
                ChapterDetail("الفصل الخامس: المعادلات التفاضلية", "فصل المتغيرات والمعادلات المتجانسة", "20 درجة وزاري"),
                ChapterDetail("الفصل السادس: الهندسة الفضائية", "المبرهنات ونتائجها والمجسمات الفضائية", "15-20 درجة وزاري")
            )
        )
        "physics" -> Pair(
            "الفيزياء — السادس العلمي",
            listOf(
                ChapterDetail("الفصل الأول: المتسعات", "العازل الكهربائي، ربط التوالي والتوازي، الطاقة والمجال الكهربائي، دائرة RC", "20-25 درجة وزاري"),
                ChapterDetail("الفصل الثاني: الحث الكهرومغناطيسي", "اكتشاف فاراداي، القوة الدافعة المحتثة، قانون لنز، الحث الذاتي والمتبادل", "20-25 درجة وزاري"),
                ChapterDetail("الفصل الثالث: دوائر التيار المتناوب", "دائرة RLC، الرنين الكهربائي، عامل القدرة وعامل النوعية", "20-25 درجة وزاري"),
                ChapterDetail("الفصل الرابع: البصريات الفيزيائية", "تداخل الضوء، تجربة شق يونك، حيود واستقطاب الضوء", "15 درجة وزاري"),
                ChapterDetail("الفصل الخامس: الفيزياء الحديثة", "إشعاع الجسم الأسود، الظاهرة الكهروضوئية، فرضية دي برولي ومبدأ اللادقة", "15 درجة وزاري"),
                ChapterDetail("الفصل السادس: إلكترونيات الحالة الصلبة", "أشباه الموصلات، الثنائي البلوري PN والترانزستور", "15 درجة وزاري"),
                ChapterDetail("الفصل السابع: الأطياف الذرية والليزر", "طيف ذرة الهيدروجين، الأشعة السينية، ليزر الهيليوم-نيون", "15 درجة وزاري"),
                ChapterDetail("الفصل الثامن: الفيزياء النووية", "طاقة الارتباط النووي، الانحلال الإشعاعي، الانشطار والاندماج", "15 درجة وزاري")
            )
        )
        "chemistry" -> Pair(
            "الكيمياء — السادس العلمي",
            listOf(
                ChapterDetail("الفصل الأول: علم الثرموداينمك", "قانون هيس، السعة الحرارية، طاقة كبس الحرة والتلقائية", "20 درجة وزاري"),
                ChapterDetail("الفصل الثاني: الاتزان الكيميائي", "ثابت الاتزان Kc و Kp، قاعدة لوشاتليه والعوامل المؤثرة", "20 درجة وزاري"),
                ChapterDetail("الفصل الثالث: الاتزان الأيوني", "التأين الذاتي للماء، الأس الهيدروجيني، الأيون المشترك ومحاليل بفر، الذوبانية", "25-30 درجة وزاري"),
                ChapterDetail("الفصل الرابع: الكيمياء الكهربائية", "جهود الأقطاب ومعادلة نيرنست، قوانين فاراداي وخلايا الطلاء", "20 درجة وزاري"),
                ChapterDetail("الفصل الخامس: الكيمياء التناسقية", "قاعدة العدد الذري الفعال، نظرية أصرة التكافؤ VBT وتسمية المعقدات", "15-20 درجة وزاري"),
                ChapterDetail("الفصل السادس: طرائق التحليل الكيميائي", "التحليل الوصفي (فصل الأيونات) والتحليل الكمي الوزني والحجمي والتسحيح", "15-20 درجة وزاري"),
                ChapterDetail("الفصل السابع: الكيمياء العضوية", "هاليدات الألكيل، الكحولات، الإيثرات، الألدهيدات والكيتونات والحوامض الكربوكسيلية", "15-20 درجة وزاري")
            )
        )
        else -> Pair(
            "اللغة العربية — السادس الإعدادي",
            listOf(
                ChapterDetail("الوحدة الأولى: أسلوب الاستفهام", "حروف وأسماء الاستفهام ودلالاتها وإعرابها والاستفهام المجازي", "10-12 درجة"),
                ChapterDetail("الوحدة الثانية: أسلوب النفي", "النفي الظاهر بالأدوات (ليس، لا، لم، لما، لن، ما، إن، غير) والنفي الضمني", "10-14 درجة"),
                ChapterDetail("الوحدة الثالثة: أسلوب الاستثناء", "أنواع الاستثناء (تام، مفرغ)، وأدوات الاستثناء وإعراب المستثنى", "6-8 درجات"),
                ChapterDetail("الوحدة الرابعة: أسلوب التوكيد", "التوكيد اللفظي والمعنوي، والتوكيد بالحروف والقصر والمدح والذم", "8-10 درجات"),
                ChapterDetail("الأدب والنصوص", "شعراء الإحياء والمهجر والشعر الحر والقصة والمقالة العراقية", "40 درجة وزاري")
            )
        )
    }

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = subjectName,
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
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Action Buttons Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigate(Screen.QuizDetail.createRoute(subjectId)) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = Navy900,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اختبار بالمادة",
                            color = Navy900,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { onNavigate(Screen.AITutor.route) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldWarm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = GoldWarm,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "سؤال الذكاء AI", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Text(
                    text = "فصول ومواضيع المنهج الرسمي:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            itemsIndexed(chapters) { index, chapter ->
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
                                text = chapter.title,
                                style = MaterialTheme.typography.titleSmall,
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
                                    text = chapter.ministerialImportance,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldWarm,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = chapter.topics,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
