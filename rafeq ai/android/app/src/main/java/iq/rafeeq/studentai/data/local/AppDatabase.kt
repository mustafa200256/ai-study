package iq.rafeeq.studentai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import iq.rafeeq.studentai.data.local.dao.*
import iq.rafeeq.studentai.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        StudyTaskEntity::class,
        NoteEntity::class,
        QuizQuestionEntity::class,
        QuizAttemptEntity::class,
        ChatMessageEntity::class,
        PaymentRequestEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun noteDao(): NoteDao
    abstract fun quizDao(): QuizDao
    abstract fun chatDao(): ChatDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rafeeq_student_ai.db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed initial Iraqi curriculum quiz questions and sample notes/tasks
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialData(database)
                    }
                }
            }
        }

        private suspend fun seedInitialData(db: AppDatabase) {
            val questions = listOf(
                // Mathematics (الرياضيات)
                QuizQuestionEntity(
                    subjectId = "math",
                    subjectName = "الرياضيات",
                    question = "إذا كانت f(x) مستمرة على [a, b] وقابلة للاشتقاق على (a, b) و f(a) = f(b)، فإن هناك قيمة c تنتمي إلى (a, b) بحيث f'(c) = 0، هذه مبرهنة:",
                    optionA = "مبرهنة رول",
                    optionB = "مبرهنة القيمة المتوسطة",
                    optionC = "مبرهنة ديموافر",
                    optionD = "مبرهنة فيثاغورس",
                    correctIndex = 0,
                    explanation = "هذا هو نص مبرهنة رول الرياضية المعتمدة في منهج السادس الإعدادي."
                ),
                QuizQuestionEntity(
                    subjectId = "math",
                    subjectName = "الرياضيات",
                    question = "ما هو ناتج تكامل الدالة ∫ cos(2x) dx ؟",
                    optionA = "2 sin(2x) + C",
                    optionB = "(1/2) sin(2x) + C",
                    optionC = "-sin(2x) + C",
                    optionD = "-(1/2) sin(2x) + C",
                    correctIndex = 1,
                    explanation = "مشتقة sin(2x) هي 2 cos(2x)، بالتالي تكامل cos(2x) هو (1/2) sin(2x) + C."
                ),
                QuizQuestionEntity(
                    subjectId = "math",
                    subjectName = "الرياضيات",
                    question = "قيمة المقدار i² في مجموعة الأعداد المركبة C تساوي:",
                    optionA = "1",
                    optionB = "-1",
                    optionC = "0",
                    optionD = "-i",
                    correctIndex = 1,
                    explanation = "الوحدة التخيلية i معرفة بحيث i² = -1."
                ),

                // Physics (الفيزياء)
                QuizQuestionEntity(
                    subjectId = "physics",
                    subjectName = "الفيزياء",
                    question = "وحدة قياس سعة المتسعة في النظام الدولي للوحدات (SI) هي:",
                    optionA = "الفولت (V)",
                    optionB = "الكولوم (C)",
                    optionC = "الفاراد (F)",
                    optionD = "الهنري (H)",
                    correctIndex = 2,
                    explanation = "الفاراد (Farad) هو وحدة سعة المتسعة، ويعادل كولوم / فولت."
                ),
                QuizQuestionEntity(
                    subjectId = "physics",
                    subjectName = "الفيزياء",
                    question = "ينص قانون لنز على أن التيار المحتث في دائرة كهربائية مقفلة يمتلك اتجاهاً بحيث:",
                    optionA = "يساعد المجال المغناطيسي المولد له دائماً",
                    optionB = "يولد مجالاً مغناطيسياً محتثاً يعاكس بتأثيره التغير في الفيض المسبب له",
                    optionC = "يكون دائماً مساوياً للصفر",
                    optionD = "يزيد من درجة حرارة السلك فقط",
                    correctIndex = 1,
                    explanation = "هذا هو نص قانون لنز الذي يمثل تطبيقاً لقانون حفظ الطاقة."
                ),

                // Chemistry (الكيمياء)
                QuizQuestionEntity(
                    subjectId = "chemistry",
                    subjectName = "الكيمياء",
                    question = "إذا كانت قيمة ΔG (طاقة كبس الحرة) لتفاعل كيميائي سالبة، فإن التفاعل يكون:",
                    optionA = "تلقائياً",
                    optionB = "غير تلقائي",
                    optionC = "في حالة اتزان تام",
                    optionD = "ماصاً للحرارة فقط",
                    correctIndex = 0,
                    explanation = "عندما تكون ΔG < 0 يكون التفاعل تلقائياً تحت الظروف المحددة."
                ),
                QuizQuestionEntity(
                    subjectId = "chemistry",
                    subjectName = "الكيمياء",
                    question = "محلول منظم (بفر) مكون من حامض الخليك وخلات الصوديوم، عند إضافة كمية قليلة من قاعدة قوية إليه فإن قيمة الأس الهيدروجيني pH:",
                    optionA = "تزداد بشكل كبير جداً",
                    optionB = "تنخفض إلى الصفر",
                    optionC = "تقاوم التغير وتبقى ثابتة تقريباً",
                    optionD = "تتحول فوراً إلى 7",
                    correctIndex = 2,
                    explanation = "المحلول البفري يقاوم التغير في قيمة الأس الهيدروجيني pH عند إضافة حمض أو قاعدة."
                ),

                // Arabic (اللغة العربية)
                QuizQuestionEntity(
                    subjectId = "arabic",
                    subjectName = "اللغة العربية",
                    question = "في قوله تعالى: (هَلْ يَسْتَوِي الَّذِينَ يَعْلَمُونَ وَالَّذِينَ لَا يَعْلَمُونَ)، الاستفهام خرج إلى غرض بلاغي هو:",
                    optionA = "التعجب",
                    optionB = "النفي الضمني",
                    optionC = "التمني",
                    optionD = "التقرير",
                    correctIndex = 1,
                    explanation = "المعنى: لا يستوي الذين يعلمون والذين لا يعلمون، فهو استفهام مجازي متضمن معنى النفي."
                ),
                QuizQuestionEntity(
                    subjectId = "arabic",
                    subjectName = "اللغة العربية",
                    question = "أداة النفي (لَن) تختص بالدخول على الفعل المضارع وتفيد:",
                    optionA = "نفي الحاضر فقط",
                    optionB = "نفي الماضي المنقطع",
                    optionC = "نفي المستقبل نفياً مؤكداً بدون قرينة",
                    optionD = "الدعاء فقط",
                    correctIndex = 2,
                    explanation = "'لن' حرف نفي ونصب واستقبال، ينفي الفعل المضارع في المستقبل نفياً مؤكداً."
                )
            )
            db.quizDao().insertQuestions(questions)

            // Seed sample welcome note
            db.noteDao().insertNote(
                NoteEntity(
                    title = "مرحباً بك في رفيق الطالب العراقي AI",
                    content = "أهلاً بك يا بطل! تم تصميم هذا التطبيق ليكون رفيقك الدائم في رحلة التفوق والنجاح بالامتحانات الوزارية. يمكنك تنظيم جدولك، حل الاختبارات، وتدوين ملاحظاتك وسؤال المدرس الذكي في أي وقت.",
                    subject = "عام"
                )
            )

            // Seed sample task
            db.taskDao().insertTask(
                StudyTaskEntity(
                    title = "مراجعة الفصل الأول: المتسعات",
                    subject = "الفيزياء",
                    dueDate = "اليوم",
                    dueTime = "20:00",
                    priority = "عالية",
                    isCompleted = false
                )
            )
        }
    }
}
