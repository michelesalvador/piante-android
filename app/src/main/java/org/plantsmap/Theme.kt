package org.plantsmap

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val textLight = Color(0xFF292A2C)
val textDark = Color(0xFFD9DEDC)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF009688),
    onPrimary = Color.White,
    background = Color(0xFFD5DEDE),
    onBackground = textLight,
    surface = Color(0xFFECF6F3),
    onSurface = textLight
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF056B61),
    onPrimary = Color.White,
    background = Color(0xFF1D2323),
    onBackground = textDark,
    surface = Color(0xFF242D2F),
    onSurface = textDark
)

val Typography = Typography(
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 48.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 20.sp
    )
)

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}