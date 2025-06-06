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
    val revBg: Color,
    val normal: Color,
    val dark: Color,
    val light: Color,
    val black : Color,
    val iconSelected: Color,
    val iconNotSelected: Color,
    val shadow: Color,
    val exitIcon: Color,
    val font: Color,
    val grayText: Color
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
    revBg = Color(0xFF464646),
    normal = Color(0xFFFEDAB8),
    dark = Color(0xFF888888),
    light = Color(0xFFEAEAEA),
    black = Color(0xFF2B2B2B),
    iconSelected = Color(0xFFFE969A),
    iconNotSelected = Color(0xFF727276),
    shadow = Color(0xFF121212),
    exitIcon = Color(0xFFFF2729),
    font = Color.Black,
    grayText = Color(0xFF8E8E92)
)

val DarkAppColors = LightAppColors.copy(
    bg = Color(0xFF2B2B2B),
    revBg = Color(0xFFF7F7F7),
    gray = Color(0xFF444444),
    lightGray = Color(0xFF555555),
    shadow = Color.White,
    font = Color.White,
    grayText = Color(0xFF8E8E92)
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
