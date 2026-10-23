package iq.rafeeq.studentai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import iq.rafeeq.studentai.ui.navigation.RafeeqNavHost
import iq.rafeeq.studentai.ui.theme.RafeeqTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            RafeeqTheme(darkTheme = isDarkTheme) {
                // Enforce Arabic RTL layout across the entire application
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RafeeqNavHost(
                            isDarkTheme = isDarkTheme,
                            onThemeToggle = { isDarkTheme = it }
                        )
                    }
                }
            }
        }
    }
}
