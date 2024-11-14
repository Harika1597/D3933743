package uk.ac.tees.mad.sq.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0F6BAA),
    secondary = Color(0xFF02635E),
    tertiary = Color.Black // Tertiary is black in dark mode
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3FB9F9),
    secondary = Color(0xFF08C3BD),
    tertiary = Color(0xFFF2F2F2) // Set tertiary to a light gray in light mode
)

@Composable
fun SmartQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    Log.d("SmartQuizTheme", "Dark Theme Enabled: $darkTheme")
    val colorScheme = when {
        // Apply dynamic colors only in light mode if supported
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !darkTheme -> {
            val context = LocalContext.current
            dynamicLightColorScheme(context)
        }
        // Use custom dark color scheme if dark mode is enabled
        darkTheme -> DarkColorScheme
        // Use custom light color scheme otherwise
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

