package com.novarixis.nebular.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension

@Composable
fun NebularTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 4,
    transparentBackground: Boolean = false,
) {
    val palette = NebularColors.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (transparentBackground) Modifier
                else Modifier
                    .background(color = palette.surfaceVariant, shape = RoundedCornerShape(NebularDimension.radiusLg))
                    .border(1.dp, palette.glassBorder, RoundedCornerShape(NebularDimension.radiusLg))
            )
            .padding(horizontal = NebularDimension.spacerSm),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = palette.onSurface),
        cursorBrush = SolidColor(palette.primary),
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.height(NebularDimension.buttonHeight).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)
            ) {
                leadingIcon?.invoke()
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, style = MaterialTheme.typography.bodyMedium, color = palette.onSurfaceMuted)
                    }
                    innerTextField()
                }
                trailingIcon?.invoke()
            }
        }
    )
}

@Composable
fun NebularSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
) {
    val palette = NebularColors.current
    NebularTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = palette.onSurfaceMuted, modifier = Modifier.size(20.dp))
        },
        trailingIcon = if (value.isNotEmpty()) {
            {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    modifier = Modifier.size(20.dp).clickable { onValueChange("") },
                    tint = palette.onSurfaceMuted
                )
            }
        } else null
    )
}
