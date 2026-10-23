package iq.rafeeq.studentai.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import iq.rafeeq.studentai.RafeeqApp
import iq.rafeeq.studentai.ui.components.RafeeqBottomBar
import iq.rafeeq.studentai.ui.screens.*

@Composable
fun RafeeqNavHost(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    val app = RafeeqApp.instance
    val studyRepo = app.studyRepository
    val chatRepo = app.chatRepository
    val aiClient = app.aiClient

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.AITutor.route,
        Screen.Curriculum.route,
        Screen.Schedule.route,
        Screen.Notes.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                RafeeqBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    studyRepository = studyRepo,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(Screen.AITutor.route) {
                AITutorScreen(
                    chatRepository = chatRepo,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Curriculum.route) {
                CurriculumScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(
                route = Screen.SubjectDetail.route,
                arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
            ) { backStackEntry ->
                val subjectId = backStackEntry.arguments?.getString("subjectId") ?: "math"
                SubjectDetailScreen(
                    subjectId = subjectId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(Screen.Schedule.route) {
                ScheduleScreen(
                    studyRepository = studyRepo
                )
            }

            composable(Screen.Quizzes.route) {
                QuizzesScreen(
                    studyRepository = studyRepo,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(
                route = Screen.QuizDetail.route,
                arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
            ) { backStackEntry ->
                val subjectId = backStackEntry.arguments?.getString("subjectId") ?: "math"
                QuizDetailScreen(
                    subjectId = subjectId,
                    studyRepository = studyRepo,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Notes.route) {
                NotesScreen(
                    studyRepository = studyRepo
                )
            }

            composable(Screen.Achievements.route) {
                AchievementsScreen(
                    studyRepository = studyRepo,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Subscription.route) {
                SubscriptionScreen(
                    studyRepository = studyRepo,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    aiClient = aiClient,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle,
                    onNavigate = { route -> navController.navigate(route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.About.route) {
                AboutScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
