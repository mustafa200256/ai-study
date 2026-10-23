package iq.rafeeq.studentai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Navy900,
    primaryContainer = Navy600,
    onPrimaryContainer = GoldWarm,
    secondary = Caramel,
    onSecondary = Navy900,
    secondaryContainer = Navy700,
    onSecondaryContainer = BeigeSoft,
    tertiary = GoldWarm,
    background = Navy800,
    onBackground = TextPrimaryDark,
    surface = Navy700,
    onSurface = TextPrimaryDark,
    surfaceVariant = Navy600,
    onSurfaceVariant = TextSecondaryDark,
    outline = Navy500,
    error = ErrorRed,
    onError = TextPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = LightSurface,
    primaryContainer = BeigeSoft,
    onPrimaryContainer = CaramelDark,
    secondary = CaramelDark,
    onSecondary = LightSurface,
    secondaryContainer = LightCard,
    onSecondaryContainer = TextPrimaryLight,
    tertiary = GoldPrimary,
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
    error = ErrorRed,
    onError = LightSurface
)

@Composable
fun RafeeqTheme(
    darkTheme: Boolean = true, // Default to luxurious dark navy mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.toArgb()
                it.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
