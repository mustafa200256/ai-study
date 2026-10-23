package iq.rafeeq.studentai.ui.navigation

sealed class Screen(val route: String, val title: String = "") {
    object Splash : Screen("splash", "البداية")
    object Home : Screen("home", "الرئيسية")
    object AITutor : Screen("ai_tutor", "المدرس الذكي AI")
    object Curriculum : Screen("curriculum", "المنهج العراقي")
    object SubjectDetail : Screen("subject_detail/{subjectId}", "تفاصيل المادة") {
        fun createRoute(subjectId: String) = "subject_detail/$subjectId"
    }
    object Schedule : Screen("schedule", "جدول الدراسة")
    object Quizzes : Screen("quizzes", "الاختبارات الوزارية")
    object QuizDetail : Screen("quiz_detail/{subjectId}", "بدء الاختبار") {
        fun createRoute(subjectId: String) = "quiz_detail/$subjectId"
    }
    object Notes : Screen("notes", "الملاحظات والملخصات")
    object Achievements : Screen("achievements", "الإنجازات والتقدم")
    object Subscription : Screen("subscription", "الاشتراك والمحفظة")
    object Settings : Screen("settings", "الإعدادات")
    object About : Screen("about", "حول التطبيق والمطور")
}
