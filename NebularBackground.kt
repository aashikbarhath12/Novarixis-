package com.novarixis.nebular.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.novarixis.nebular.core.ui.theme.NebularColors
import kotlin.random.Random

/**
 * Full-screen backdrop: soft vertical gradient plus a sprinkle of star dots
 * (very subtle in light mode, a bit more visible in dark mode) — matches the
 * atmosphere of both reference mocks without overdoing gradients.
 */
@Composable
fun NebularBackground(
    modifier: Modifier = Modifier,
    showStars: Boolean = true,
    content: @Composable () -> Unit
) {
    val palette = NebularColors.current
    val stars = remember(palette.isDark) {
        val rnd = Random(42)
        List(if (palette.isDark) 45 else 18) {
            Triple(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat() * 1.6f + 0.6f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(palette.backgroundGradientTop, palette.backgroundGradientBottom)
                )
            )
    ) {
        if (showStars) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val starAlpha = if (palette.isDark) 0.8f else 0.35f
                stars.forEach { (fx, fy, radius) ->
                    drawCircle(
                        color = Color.White.copy(alpha = starAlpha),
                        radius = radius,
                        center = Offset(size.width * fx, size.height * fy)
                    )
                }
            }
        }
        content()
    }
}
