package com.novarixis.nebular.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension

enum class NebularButtonStyle { PRIMARY, SECONDARY, GHOST }
enum class NebularButtonSize { SMALL, MEDIUM, LARGE }

@Composable
fun NebularButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    style: NebularButtonStyle = NebularButtonStyle.PRIMARY,
    size: NebularButtonSize = NebularButtonSize.MEDIUM,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val palette = NebularColors.current

    val backgroundColor = when (style) {
        NebularButtonStyle.PRIMARY -> palette.primary
        NebularButtonStyle.SECONDARY -> palette.surfaceVariant
        NebularButtonStyle.GHOST -> Color.Transparent
    }
    val contentColor = when (style) {
        NebularButtonStyle.PRIMARY -> Color.White
        NebularButtonStyle.SECONDARY -> palette.primary
        NebularButtonStyle.GHOST -> palette.primary
    }

    val height = when (size) {
        NebularButtonSize.SMALL -> NebularDimension.buttonHeightSmall
        NebularButtonSize.MEDIUM -> NebularDimension.buttonHeight
        NebularButtonSize.LARGE -> NebularDimension.buttonHeightLarge
    }

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .height(height)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(),
                enabled = enabled && !isLoading,
                onClick = onClick
            )
            .background(
                color = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(NebularDimension.radiusLg)
            )
            .padding(horizontal = NebularDimension.spacerLg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.height(18.dp).width(18.dp), color = contentColor, strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(NebularDimension.spacerSm))
        } else if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(NebularDimension.spacerSm))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f),
            fontWeight = FontWeight.SemiBold
        )

        if (trailingIcon != null && !isLoading) {
            Spacer(modifier = Modifier.width(NebularDimension.spacerSm))
            trailingIcon()
        }
    }
}

@Composable
fun NebularIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    size: androidx.compose.ui.unit.Dp = NebularDimension.buttonHeight,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .height(size)
            .width(size)
            .clickable(enabled = enabled, onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(NebularDimension.radiusLg)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon()
    }
}
