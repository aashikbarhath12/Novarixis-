package com.novarixis.nebular.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension
import com.novarixis.nebular.domain.model.ChatMessage
import com.novarixis.nebular.domain.model.MessageRole

@Composable
fun ChatMessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    val palette = NebularColors.current
    val isUser = message.role == MessageRole.USER

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = NebularDimension.radiusLg,
                        topEnd = NebularDimension.radiusLg,
                        bottomStart = if (isUser) NebularDimension.radiusLg else NebularDimension.radiusSm,
                        bottomEnd = if (isUser) NebularDimension.radiusSm else NebularDimension.radiusLg
                    )
                )
                .background(if (isUser) palette.userBubble else palette.surfaceVariant)
                .widthIn(max = 300.dp)
                .padding(NebularDimension.spacerMd)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) Color.White else palette.onSurface
            )
        }
    }
}

@Composable
fun ChatResponseActions(
    modifier: Modifier = Modifier,
    onCopy: () -> Unit = {},
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {},
    onShare: () -> Unit = {},
    onMore: () -> Unit = {},
) {
    val palette = NebularColors.current
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
        ResponseActionIcon(Icons.Default.ContentCopy, "Copy", palette.onSurfaceMuted, onCopy)
        ResponseActionIcon(Icons.Default.ThumbUp, "Like", palette.onSurfaceMuted, onLike)
        ResponseActionIcon(Icons.Default.ThumbDown, "Dislike", palette.onSurfaceMuted, onDislike)
        ResponseActionIcon(Icons.Default.Share, "Share", palette.onSurfaceMuted, onShare)
        ResponseActionIcon(Icons.Default.MoreVert, "More", palette.onSurfaceMuted, onMore)
    }
}

@Composable
private fun ResponseActionIcon(icon: ImageVector, description: String, tint: Color, onClick: () -> Unit) {
    Icon(
        imageVector = icon,
        contentDescription = description,
        modifier = Modifier.size(19.dp).clickable(onClick = onClick),
        tint = tint
    )
}

@Composable
fun SuggestedPromptChip(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val palette = NebularColors.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(NebularDimension.radiusFull))
            .background(palette.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = NebularDimension.spacerMd, vertical = NebularDimension.spacerXs)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = palette.onSurface)
    }
}

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val palette = NebularColors.current
    val transition = rememberInfiniteTransition(label = "typing")

    Row(modifier = modifier.padding(NebularDimension.spacerSm), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(3) { index ->
            val delay = index * 150
            val alpha by transition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = delay, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot$index"
            )
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(palette.onSurfaceMuted.copy(alpha = alpha))
            )
        }
    }
}
