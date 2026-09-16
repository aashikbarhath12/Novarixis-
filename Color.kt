package com.novarixis.nebular.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Dark theme palette — matches the dark reference mock:
 * deep space navy background, electric cyan/blue glow, white type.
 */
object DarkColors {
    val Background = Color(0xFF060A1A)
    val BackgroundGradientTop = Color(0xFF0A1330)
    val BackgroundGradientBottom = Color(0xFF040713)
    val Surface = Color(0xFF121A33)
    val SurfaceVariant = Color(0xFF1B2545)
    val SurfaceElevated = Color(0xFF212C50)

    val Primary = Color(0xFF2E9BFF)      // electric blue
    val PrimaryDark = Color(0xFF1B6FCC)
    val Accent = Color(0xFF5CE1FF)       // cyan highlight
    val AccentGlow = Color(0xFF7FE8FF)

    val OnBackground = Color(0xFFF3F6FF)
    val OnSurface = Color(0xFFE3E9FB)
    val OnSurfaceMuted = Color(0xFF8993B8)

    val Success = Color(0xFF2ED8A7)
    val Warning = Color(0xFFF5A623)
    val Error = Color(0xFFFF5C6C)

    val GlassBorder = Color(0x3D5CE1FF)
    val GlassFill = Color(0x142E9BFF)
    val UserBubble = Color(0xFF2E7BFF)
}

/**
 * Light theme palette — matches the light reference mock:
 * near-white / pale blue background, navy type, blue-cyan accents.
 */
object LightColors {
    val Background = Color(0xFFF3F8FF)
    val BackgroundGradientTop = Color(0xFFFFFFFF)
    val BackgroundGradientBottom = Color(0xFFE7F1FF)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFEEF4FF)
    val SurfaceElevated = Color(0xFFFFFFFF)

    val Primary = Color(0xFF0B63E5)
    val PrimaryDark = Color(0xFF0A2A66)
    val Accent = Color(0xFF12A6DA)
    val AccentGlow = Color(0xFF62C9F5)

    val OnBackground = Color(0xFF0A1330)
    val OnSurface = Color(0xFF12193A)
    val OnSurfaceMuted = Color(0xFF667089)

    val Success = Color(0xFF149E77)
    val Warning = Color(0xFFB4740B)
    val Error = Color(0xFFD53C4D)

    val GlassBorder = Color(0x330B63E5)
    val GlassFill = Color(0x0A0B63E5)
    val UserBubble = Color(0xFF0B63E5)
}

/** Distinct accent colors per model, used for icon backgrounds in selector/chips. */
object ModelColors {
    val BlueMoon = Color(0xFF2E9BFF)
    val StarPro = Color(0xFF7C6CFF)
    val NovaVision = Color(0xFF12C7D6)
    val StellerPlus = Color(0xFFB25CFF)
    val OrionCode = Color(0xFF8B5CF6)
}
