package com.snapshot.res.modifier

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val main: Color,
    val serve: Color,
    val serve2: Color,
    val white: Color,
    val gray: Color,
    val lightGray: Color,
    val blue: Color,
    val bg: Color,
    val normal: Color,
    val dark: Color,
    val light: Color,
    val black : Color
)

val LightAppColors = AppColorScheme(
    main = Color(0xFFFF8F97),
    serve = Color(0xFFA55B4B),
    serve2 = Color(0xFFB49886),
    white = Color(0xFFFFFFFF),
    gray = Color(0xFFE3E3E5),
    lightGray = Color(0xFFEFEFEF),
    blue = Color(0xFF1C78E3),
    bg = Color(0xFFF7F7F7),
    normal = Color(0xFFFEDAB8),
    dark = Color(0xFF888888),
    light = Color(0xFFEAEAEA),
    black = Color(0xFF2B2B2B),
)

val DarkAppColors = LightAppColors.copy(
    bg = Color(0xFF2B2B2B),
    gray = Color(0xFF444444),
    lightGray = Color(0xFF555555),
)

internal val LocalColors = staticCompositionLocalOf { LightAppColors }

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkAppColors else LightAppColors
    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}


object ColorTheme {
    val colors: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}
