package com.novarixis.nebular.core.ui.graphics

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.theme.NebularColors

/**
 * The Nebular app mark: a woven infinity/nebula knot glowing at the center,
 * matching the reference app-icon artwork. Pure vector so it scales cleanly
 * from launcher-icon size up to splash-screen size.
 */
@Composable
fun NebularLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    glow: Boolean = true
) {
    val palette = NebularColors.current

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        if (glow) {
            Box(
                modifier = Modifier
                    .size(size * 0.9f)
                    .blur(size * 0.35f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(palette.accentGlow.copy(alpha = 0.55f), Color.Transparent)
                        )
                    )
            )
        }

        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val r = this.size.minDimension / 2f

            val petalCount = 8
            for (i in 0 until petalCount) {
                val angle = (360f / petalCount) * i
                rotate(degrees = angle, pivot = center) {
                    val path = Path().apply {
                        moveTo(center.x, center.y - r * 0.08f)
                        cubicTo(
                            center.x + r * 0.55f, center.y - r * 0.55f,
                            center.x + r * 0.85f, center.y - r * 0.15f,
                            center.x + r * 0.92f, center.y
                        )
                        cubicTo(
                            center.x + r * 0.85f, center.y + r * 0.15f,
                            center.x + r * 0.55f, center.y + r * 0.55f,
                            center.x, center.y + r * 0.08f
                        )
                    }
                    drawPath(
                        path = path,
                        color = palette.primary.copy(alpha = 0.85f),
                        style = Stroke(width = r * 0.045f, cap = StrokeCap.Round)
                    )
                }
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, palette.accentGlow, Color.Transparent),
                    radius = r * 0.4f,
                    center = center
                ),
                radius = r * 0.4f,
                center = center
            )
        }
    }
}

/**
 * The saturn-like glowing orb used on splash/onboarding — a sphere with a ring,
 * matching the reference illustration.
 */
@Composable
fun NebularOrb(
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    animate: Boolean = true
) {
    val palette = NebularColors.current
    val infinite = rememberInfiniteTransition(label = "orb")
    val ringRotation = if (animate) {
        val anim by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(animation = tween(18000, easing = LinearEasing)),
            label = "ringRotation"
        )
        anim
    } else {
        0f
    }

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(size * 1.15f)
                .blur(size * 0.25f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(palette.accentGlow.copy(alpha = 0.35f), Color.Transparent)
                    )
                )
        )

        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val r = this.size.minDimension / 2.6f

            withTransform({ rotate(degrees = ringRotation * 0.15f, pivot = center) }) {
                drawOval(
                    color = palette.accentGlow.copy(alpha = 0.5f),
                    topLeft = Offset(center.x - r * 1.7f, center.y - r * 0.32f),
                    size = Size(r * 3.4f, r * 0.64f),
                    style = Stroke(width = r * 0.045f)
                )
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        palette.accent,
                        palette.primary,
                        palette.primaryDark
                    ),
                    center = Offset(center.x - r * 0.3f, center.y - r * 0.3f),
                    radius = r * 1.6f
                ),
                radius = r,
                center = center
            )

            for (i in 1..3) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = r * (0.4f + i * 0.18f),
                    center = center,
                    style = Stroke(width = 1.2f)
                )
            }

            withTransform({ rotate(degrees = ringRotation * 0.15f, pivot = center) }) {
                drawOval(
                    color = palette.accentGlow.copy(alpha = 0.85f),
                    topLeft = Offset(center.x - r * 1.7f, center.y + r * 0.02f),
                    size = Size(r * 3.4f, r * 0.3f),
                    style = Stroke(width = r * 0.05f)
                )
            }

            val starSeedPositions = listOf(
                0.15f to 0.1f, 0.85f to 0.15f, 0.9f to 0.8f,
                0.1f to 0.85f, 0.05f to 0.5f, 0.95f to 0.5f
            )
            starSeedPositions.forEach { (fx, fy) ->
                drawCircle(
                    color = Color.White.copy(alpha = 0.7f),
                    radius = 1.6f,
                    center = Offset(this.size.width * fx, this.size.height * fy)
                )
            }
        }
    }
}

/** Small glowing dot-icon used as a generic model avatar when no custom icon is set. */
@Composable
fun ModelGlowIcon(
    modifier: Modifier = Modifier,
    color: Color,
    size: Dp = 40.dp
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(size)
                .blur(size * 0.2f)
                .background(Brush.radialGradient(colors = listOf(color.copy(alpha = 0.6f), Color.Transparent)))
        )
        Canvas(modifier = Modifier.size(size * 0.7f)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, color)
                )
            )
        }
    }
}
