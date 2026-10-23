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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.rafeeq.studentai.ui.components.RafeeqTopBar
import iq.rafeeq.studentai.ui.navigation.Screen
import iq.rafeeq.studentai.ui.theme.*

data class SubjectItem(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val chaptersCount: Int,
    val color: Color,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumScreen(
    onNavigate: (String) -> Unit
) {
    var selectedStageIndex by remember { mutableIntStateOf(0) }
    var selectedBranchIndex by remember { mutableIntStateOf(0) }

    val stages = listOf("السادس الإعدادي", "الثالث المتوسط", "الخامس العلمي")
    val branches = listOf("العلمي (الأحيائي والتطبيقي)", "الأدبي")

    val scientificSubjects = listOf(
        SubjectItem("math", "الرياضيات", Icons.Default.Calculate, 6, GoldPrimary, "الأعداد المركبة، التفاضل، التكامل، الهندسة الفضائية"),
        SubjectItem("physics", "الفيزياء", Icons.Default.Bolt, 8, WarningOrange, "المتسعات، الحث الكهرومغناطيسي، البصريات، الفيزياء الحديثة"),
        SubjectItem("chemistry", "الكيمياء", Icons.Default.Science, 7, SuccessGreen, "الثرموداينمك، الاتزان الكيميائي، المحاليل، الكيمياء العضوية"),
        SubjectItem("biology", "الأحياء", Icons.Default.Spa, 5, Color(0xFF10B981), "الخلية، الأنسجة، التكاثر، الوراثة والتطور"),
        SubjectItem("arabic", "اللغة العربية", Icons.Default.AutoStories, 6, Caramel, "قواعد اللغة (الاستفهام، النفي، الاستثناء، التوكيد) والأدب والنصوص"),
        SubjectItem("english", "اللغة الإنجليزية", Icons.Default.Translate, 8, InfoBlue, "Grammar, Reading comprehension, Stories & Writing"),
        SubjectItem("islamic", "التربية الإسلامية", Icons.Default.MenuBook, 5, GoldWarm, "أحكام التلاوة، السور القرآنية، الأحاديث الشريفة، الأبحاث")
    )

    val literarySubjects = listOf(
        SubjectItem("arabic", "اللغة العربية", Icons.Default.AutoStories, 7, Caramel, "القواعد الموسعة والأدب والنقد الأدبي"),
        SubjectItem("history", "التاريخ الحديث", Icons.Default.HistoryEdu, 6, WarningOrange, "تاريخ الوطن العربي والعراق الحديث والمعاصر"),
        SubjectItem("geography", "الجغرافيا البشرية", Icons.Default.Public, 6, SuccessGreen, "جغرافية السكان، المدن، الصناعة، التخطيط الإقليمي"),
        SubjectItem("economics", "الاقتصاد", Icons.Default.MonetizationOn, 4, GoldPrimary, "المفاهيم الاقتصادية والدخل القومي والسياسات المالية"),
        SubjectItem("english", "اللغة الإنجليزية", Icons.Default.Translate, 8, InfoBlue, "Grammar, Vocabulary, Literature & Composition"),
        SubjectItem("math", "الرياضيات الأدبية", Icons.Default.Calculate, 4, BeigeSoft, "المصفوفات، التفاضل المبسط، الإحصاء والاحتمالات")
    )

    val displayedSubjects = if (selectedBranchIndex == 0) scientificSubjects else literarySubjects

    Scaffold(
        topBar = {
            RafeeqTopBar(
                title = "المنهج الدراسي العراقي",
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
            // Stage Selection Dropdown / Segmented buttons
            item {
                Text(
                    text = "المرحلة الدراسية:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stages.forEachIndexed { index, stage ->
                        FilterChip(
                            selected = selectedStageIndex == index,
                            onClick = { selectedStageIndex = index },
                            label = { Text(stage, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Navy900
                            )
                        )
                    }
                }
            }

            if (selectedStageIndex == 0) {
                // Branch selection
                item {
                    Text(
                        text = "الفرع:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        branches.forEachIndexed { index, branch ->
                            FilterChip(
                                selected = selectedBranchIndex == index,
                                onClick = { selectedBranchIndex = index },
                                label = { Text(branch, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Caramel,
                                    selectedLabelColor = Navy900
                                )
                            )
                        }
                    }
                }
            }

            // Disclaimer banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Navy700)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = GoldWarm,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "المحتوى مصنف وفق هيكل وزارة التربية العراقية، ويتم تحديث الفصول والأسئلة باستمرار.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BeigeSoft
                        )
                    }
                }
            }

            // Subjects list
            items(displayedSubjects) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = {
                        onNavigate(Screen.SubjectDetail.createRoute(subject.id))
                    }
                )
            }
        }
    }
}

@Composable
fun SubjectCard(
    subject: SubjectItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
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
                    .size(50.dp)
                    .background(subject.color.copy(alpha = 0.15f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = subject.icon,
                    contentDescription = subject.name,
                    tint = subject.color,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${subject.chaptersCount} فصول دراسية شاملة",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldWarm
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subject.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
