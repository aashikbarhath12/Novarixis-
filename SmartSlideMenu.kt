package com.novarixis.nebular.feature.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
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
import com.novarixis.nebular.core.ui.components.NebularButton
import com.novarixis.nebular.core.ui.components.NebularButtonSize
import com.novarixis.nebular.core.ui.components.NebularButtonStyle
import com.novarixis.nebular.core.ui.graphics.NebularLogoMark
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension

@Composable
fun SmartSlideMenu(
    visible: Boolean,
    onDismiss: () -> Unit,
    userName: String = "Vasim Akthir",
    userEmail: String = "vasim@email.com",
    planLabel: String = "Free Plan",
    onNewChat: () -> Unit = {},
    onExplore: () -> Unit = {},
    onCreate: () -> Unit = {},
    onBlueCode: () -> Unit = {},
    onLibrary: () -> Unit = {},
    onHistory: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHelp: () -> Unit = {},
    onWhatsNew: () -> Unit = {},
    onLogOut: () -> Unit = {},
    onUpgrade: () -> Unit = {},
) {
    val palette = NebularColors.current

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)),
        exit = fadeOut(tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onDismiss)
        )
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(tween(280)) { -it },
        exit = slideOutHorizontally(tween(220)) { -it }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(288.dp)
                .background(palette.surface)
                .padding(NebularDimension.spacerMd)
        ) {
            // Profile header
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
                Box(
                    modifier = Modifier.size(48.dp).background(palette.primary.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = userName.take(1), style = MaterialTheme.typography.titleMedium, color = palette.primary, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = userName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
                    Text(text = userEmail, style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceMuted)
                }
            }

            Spacer(modifier = Modifier.height(NebularDimension.spacerLg))

            // Plan card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.surfaceVariant, RoundedCornerShape(NebularDimension.radiusMd))
                    .padding(NebularDimension.spacerMd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = planLabel, style = MaterialTheme.typography.labelLarge, color = palette.onSurface, fontWeight = FontWeight.SemiBold)
                NebularButton(text = "Upgrade", onClick = onUpgrade, size = NebularButtonSize.SMALL, style = NebularButtonStyle.PRIMARY)
            }

            Spacer(modifier = Modifier.height(NebularDimension.spacerLg))

            MenuItem(Icons.Default.Add, "New Chat", onNewChat)
            MenuItem(Icons.Default.Search, "Explore", onExplore)
            MenuItem(Icons.Default.AutoAwesome, "Create", onCreate)
            MenuItem(Icons.Default.Code, "Blue Code", onBlueCode)
            MenuItem(Icons.Default.LibraryBooks, "Library", onLibrary)
            MenuItem(Icons.Default.History, "History", onHistory)

            Divider(modifier = Modifier.padding(vertical = NebularDimension.spacerSm), color = palette.glassBorder)

            MenuItem(Icons.Default.Settings, "Settings", onSettingsClick)
            MenuItem(Icons.Default.HelpOutline, "Help & Support", onHelp)
            MenuItem(Icons.Default.NewReleases, "What's New", onWhatsNew)
            MenuItem(Icons.Default.ExitToApp, "Log Out", onLogOut)

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
                NebularLogoMark(size = 28.dp, glow = false)
                Column {
                    Row {
                        Text(text = "Nebular ", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
                        Text(text = "v1.0.0", style = MaterialTheme.typography.labelSmall, color = palette.onSurfaceMuted)
                    }
                    Text(text = "A Brighter Tomorrow", style = MaterialTheme.typography.labelSmall, color = palette.onSurfaceMuted)
                }
            }
        }
    }
}

@Composable
private fun MenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    val palette = NebularColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = NebularDimension.spacerSm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = palette.onSurfaceMuted, modifier = Modifier.size(21.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = palette.onSurface)
    }
}
