package com.novarixis.nebular.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novarixis.nebular.core.ui.components.NebularBackTopAppBar
import com.novarixis.nebular.core.ui.components.NebularBackground
import com.novarixis.nebular.core.ui.graphics.NebularLogoMark
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val palette = NebularColors.current

    NebularBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            NebularBackTopAppBar(title = "Settings", onBackClick = onBackClick)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(NebularDimension.spacerMd),
                verticalArrangement = Arrangement.spacedBy(NebularDimension.spacerLg)
            ) {
                item { ProfileHeader(name = "Vasim Akthir", email = "vasim@email.com") }

                item {
                    SettingsSection("Account") {
                        SettingItemClickable(Icons.Default.Person, "Account", "Manage your account")
                        SettingItemClickable(Icons.Default.CreditCard, "Subscription", uiState.planLabel)
                        SettingItemClickable(Icons.Default.Payment, "Payment Methods", "")
                    }
                }

                item {
                    SettingsSection("Preferences") {
                        SettingItemToggle(Icons.Default.Brightness4, "Dark Mode", uiState.darkMode) { viewModel.setDarkMode(it) }
                        SettingItemClickable(Icons.Default.Language, "Language", uiState.language)
                        SettingItemToggle(Icons.Default.Notifications, "Notifications", uiState.notificationsEnabled) { viewModel.setNotifications(it) }
                    }
                }

                item {
                    SettingsSection("Privacy") {
                        SettingItemClickable(Icons.Default.Security, "Privacy & Security", "Manage your data")
                    }
                }

                item {
                    SettingsSection("About") {
                        SettingItemInfo(Icons.Default.Info, "About Nebular", "v1.0.0")
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = NebularDimension.spacerLg),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NebularLogoMark(size = 22.dp, glow = false)
                        Text(
                            text = "  Nebular  v1.0.0",
                            style = MaterialTheme.typography.labelMedium,
                            color = palette.onSurfaceMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String) {
    val palette = NebularColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.size(56.dp).background(palette.primary.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = name.take(1), style = MaterialTheme.typography.titleLarge, color = palette.primary, fontWeight = FontWeight.Bold)
        }
        Column {
            Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
            Text(text = email, style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceMuted)
            Text(text = "Manage your account", style = MaterialTheme.typography.labelSmall, color = palette.primary)
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable Column.() -> Unit) {
    val palette = NebularColors.current
    Column(verticalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = palette.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(palette.surface, RoundedCornerShape(NebularDimension.radiusLg))
                .padding(vertical = 4.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingItemClickable(icon: ImageVector, title: String, subtitle: String = "", onClick: (() -> Unit)? = null) {
    val palette = NebularColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = NebularDimension.spacerMd, vertical = NebularDimension.spacerSm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(22.dp), tint = palette.primary)
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = palette.onSurface)
                if (subtitle.isNotEmpty()) {
                    Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = palette.onSurfaceMuted)
                }
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Navigate", modifier = Modifier.size(20.dp), tint = palette.onSurfaceMuted)
    }
}

@Composable
private fun SettingItemToggle(icon: ImageVector, title: String, isChecked: Boolean, onToggle: (Boolean) -> Unit) {
    val palette = NebularColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isChecked) }
            .padding(horizontal = NebularDimension.spacerMd, vertical = NebularDimension.spacerSm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(22.dp), tint = palette.primary)
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = palette.onSurface)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = palette.primary, checkedTrackColor = palette.primary.copy(alpha = 0.35f))
        )
    }
}

@Composable
private fun SettingItemInfo(icon: ImageVector, title: String, subtitle: String = "") {
    val palette = NebularColors.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = NebularDimension.spacerMd, vertical = NebularDimension.spacerSm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(22.dp), tint = palette.primary)
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = palette.onSurface)
                if (subtitle.isNotEmpty()) Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = palette.onSurfaceMuted)
            }
        }
    }
}
