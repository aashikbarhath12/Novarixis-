package com.novarixis.nebular.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension

@Composable
fun NebularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = NebularColors.current.surface,
    borderColor: Color = NebularColors.current.glassBorder,
    showBorder: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(NebularDimension.radiusLg))
            .background(color = backgroundColor, shape = RoundedCornerShape(NebularDimension.radiusLg))
            .then(
                if (showBorder) Modifier.border(1.dp, borderColor, RoundedCornerShape(NebularDimension.radiusLg))
                else Modifier
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding)
    ) {
        content()
    }
}

@Composable
fun NebularFeatureCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = NebularColors.current
    NebularCard(
        modifier = modifier.fillMaxWidth().height(NebularDimension.featureCardHeight),
        onClick = onClick,
        backgroundColor = palette.surfaceVariant
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(NebularDimension.spacerMd),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.onSurfaceMuted
                    )
                }
                Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) { icon() }
            }
        }
    }
}

@Composable
fun NebularModelCard(
    icon: @Composable () -> Unit,
    name: String,
    description: String,
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = NebularColors.current
    val backgroundColor = if (isSelected) palette.primary.copy(alpha = if (palette.isDark) 0.16f else 0.08f) else palette.surface
    val borderColor = if (isSelected) palette.primary else palette.glassBorder

    NebularCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        backgroundColor = backgroundColor,
        borderColor = borderColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(NebularDimension.spacerMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd)
            ) {
                Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) { icon() }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
                    Text(text = description, style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceMuted)
                    Text(text = category, style = MaterialTheme.typography.labelSmall, color = palette.primary)
                }
            }
            if (isSelected) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Selected", tint = palette.primary, modifier = Modifier.size(22.dp))
            }
        }
    }
}
