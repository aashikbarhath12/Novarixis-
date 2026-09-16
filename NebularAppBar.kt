package com.novarixis.nebular.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension

@Composable
fun NebularTopAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    navigationIcon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(NebularDimension.topBarHeight)
            .padding(horizontal = NebularDimension.spacerMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
            navigationIcon?.invoke()
            title?.invoke()
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
            actions?.invoke(this)
        }
    }
}

@Composable
fun NebularBackTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val palette = NebularColors.current
    NebularTopAppBar(
        modifier = modifier,
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp).clickable(onClick = onBackClick),
                tint = palette.onSurface
            )
        },
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
        },
        actions = actions
    )
}

data class BottomNavItem(val icon: ImageVector, val label: String)

@Composable
fun NebularBottomNavigation(
    selectedIndex: Int,
    items: List<BottomNavItem>,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = NebularColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(NebularDimension.bottomNavHeight)
            .background(palette.surface)
            .padding(horizontal = NebularDimension.spacerSm),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            val color = if (isSelected) palette.primary else palette.onSurfaceMuted
            Column(
                modifier = Modifier.weight(1f).clickable { onItemSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = item.icon, contentDescription = item.label, tint = color, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = item.label, style = MaterialTheme.typography.labelSmall, color = color)
            }
        }
    }
}
