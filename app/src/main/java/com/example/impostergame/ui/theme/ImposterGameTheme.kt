package com.example.impostergame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

// Color Scheme Values
private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFFB3261E), // Red accent
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    error = Color(0xFFB3261E),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFF6750A4), // Purple accent
    tertiary = Color(0xFFEFB8C8),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    error = Color(0xFFCF6679),
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFF332D41),
    onTertiary = Color(0xFF492532),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)

// Theme Typography
private val Typography = androidx.compose.material3.Typography(
    // Use the default typography for Material 3
    bodyLarge = TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal
    )
)

// Theme Shapes
private val Shapes = androidx.compose.material3.Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

@Composable
fun ImposterGameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}