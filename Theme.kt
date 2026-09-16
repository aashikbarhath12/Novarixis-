package com.novarixis.nebular.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Palette shape so screens/components can read `NebularColors.current.xxx` regardless of theme. */
data class NebularPalette(
    val background: Color,
    val backgroundGradientTop: Color,
    val backgroundGradientBottom: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,
    val primary: Color,
    val primaryDark: Color,
    val accent: Color,
    val accentGlow: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val glassBorder: Color,
    val glassFill: Color,
    val userBubble: Color,
    val isDark: Boolean
)

private val darkPalette = NebularPalette(
    background = DarkColors.Background,
    backgroundGradientTop = DarkColors.BackgroundGradientTop,
    backgroundGradientBottom = DarkColors.BackgroundGradientBottom,
    surface = DarkColors.Surface,
    surfaceVariant = DarkColors.SurfaceVariant,
    surfaceElevated = DarkColors.SurfaceElevated,
    primary = DarkColors.Primary,
    primaryDark = DarkColors.PrimaryDark,
    accent = DarkColors.Accent,
    accentGlow = DarkColors.AccentGlow,
    onBackground = DarkColors.OnBackground,
    onSurface = DarkColors.OnSurface,
    onSurfaceMuted = DarkColors.OnSurfaceMuted,
    success = DarkColors.Success,
    warning = DarkColors.Warning,
    error = DarkColors.Error,
    glassBorder = DarkColors.GlassBorder,
    glassFill = DarkColors.GlassFill,
    userBubble = DarkColors.UserBubble,
    isDark = true
)

private val lightPalette = NebularPalette(
    background = LightColors.Background,
    backgroundGradientTop = LightColors.BackgroundGradientTop,
    backgroundGradientBottom = LightColors.BackgroundGradientBottom,
    surface = LightColors.Surface,
    surfaceVariant = LightColors.SurfaceVariant,
    surfaceElevated = LightColors.SurfaceElevated,
    primary = LightColors.Primary,
    primaryDark = LightColors.PrimaryDark,
    accent = LightColors.Accent,
    accentGlow = LightColors.AccentGlow,
    onBackground = LightColors.OnBackground,
    onSurface = LightColors.OnSurface,
    onSurfaceMuted = LightColors.OnSurfaceMuted,
    success = LightColors.Success,
    warning = LightColors.Warning,
    error = LightColors.Error,
    glassBorder = LightColors.GlassBorder,
    glassFill = LightColors.GlassFill,
    userBubble = LightColors.UserBubble,
    isDark = false
)

val LocalNebularPalette = staticCompositionLocalOf { darkPalette }

/** Convenience accessor: `NebularColors.current.primary` */
object NebularColors {
    val current: NebularPalette
        @Composable get() = LocalNebularPalette.current
}

@Composable
fun NebularTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val palette = if (darkTheme) darkPalette else lightPalette

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = Color.White,
            secondary = palette.accent,
            onSecondary = Color.Black,
            background = palette.background,
            onBackground = palette.onBackground,
            surface = palette.surface,
            onSurface = palette.onSurface,
            surfaceVariant = palette.surfaceVariant,
            onSurfaceVariant = palette.onSurfaceMuted,
            error = palette.error,
            onError = Color.White,
            outline = palette.glassBorder
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = Color.White,
            secondary = palette.accent,
            onSecondary = Color.White,
            background = palette.background,
            onBackground = palette.onBackground,
            surface = palette.surface,
            onSurface = palette.onSurface,
            surfaceVariant = palette.surfaceVariant,
            onSurfaceVariant = palette.onSurfaceMuted,
            error = palette.error,
            onError = Color.White,
            outline = palette.glassBorder
        )
    }

    CompositionLocalProvider(LocalNebularPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = NebularTypography,
            content = content
        )
    }
}
