package com.novarixis.nebular.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novarixis.nebular.core.ui.components.BottomNavItem
import com.novarixis.nebular.core.ui.components.NebularBackground
import com.novarixis.nebular.core.ui.components.NebularBottomNavigation
import com.novarixis.nebular.core.ui.components.NebularCard
import com.novarixis.nebular.core.ui.components.NebularFeatureCard
import com.novarixis.nebular.core.ui.components.NebularIconButton
import com.novarixis.nebular.core.ui.components.NebularTextField
import com.novarixis.nebular.core.ui.graphics.NebularLogoMark
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension
import com.novarixis.nebular.feature.menu.SmartSlideMenu

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToChat: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val palette = NebularColors.current

    NebularBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar(
                onMenuClick = { showMenu = true },
                planLabel = uiState.planType
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(NebularDimension.spacerMd),
                horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd),
                verticalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd)
            ) {
                item(span = { GridItemSpan(2) }) { Greeting(uiState.userName) }
                item(span = { GridItemSpan(2) }) { BlueMoonFeatureCard(onClick = { onNavigateToChat("new") }) }

                item {
                    FeatureCardItem(Icons.Default.Chat, "Chat", "Ask anything") { onNavigateToChat("new") }
                }
                item {
                    FeatureCardItem(Icons.Default.AutoAwesome, "Create", "Images, docs, code") { }
                }
                item {
                    FeatureCardItem(Icons.Default.FileOpen, "Analyze", "Files, data, PDF") { }
                }
                item {
                    FeatureCardItem(Icons.Default.Search, "Explore", "Web, videos, research") { }
                }
                item {
                    FeatureCardItem(Icons.Default.Code, "Blue Code", "Build, debug, run") { }
                }
                item {
                    FeatureCardItem(Icons.Default.Psychology, "Agents", "Automate your work") { }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = NebularDimension.spacerMd, vertical = NebularDimension.spacerSm)
            ) {
                HomeChatInputBar(onSend = { onNavigateToChat("new") })
            }

            NebularBottomNavigation(
                selectedIndex = selectedTab,
                items = listOf(
                    BottomNavItem(Icons.Default.Home, "Home"),
                    BottomNavItem(Icons.Default.Search, "Explore"),
                    BottomNavItem(Icons.Default.Code, "Blue Code"),
                    BottomNavItem(Icons.Default.MenuBook, "Library"),
                    BottomNavItem(Icons.Default.Person, "Profile")
                ),
                onItemSelected = { index ->
                    selectedTab = index
                    if (index == 4) onNavigateToSettings()
                }
            )
        }

        SmartSlideMenu(
            visible = showMenu,
            onDismiss = { showMenu = false },
            userName = uiState.userName,
            onSettingsClick = { showMenu = false; onNavigateToSettings() },
            onNewChat = { showMenu = false; onNavigateToChat("new") }
        )
    }
}

@Composable
private fun HomeTopBar(onMenuClick: () -> Unit, planLabel: String) {
    val palette = NebularColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NebularDimension.spacerMd, vertical = NebularDimension.spacerSm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(24.dp).clickable(onClick = onMenuClick),
                tint = palette.onSurface
            )
            NebularLogoMark(size = 26.dp, glow = false)
            Text(text = "Nebular", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = palette.onSurface)
        }

        Box(
            modifier = Modifier
                .background(palette.primary.copy(alpha = 0.12f), RoundedCornerShape(NebularDimension.radiusSm))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = planLabel, style = MaterialTheme.typography.labelSmall, color = palette.primary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun Greeting(userName: String) {
    val palette = NebularColors.current
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = NebularDimension.spacerSm)) {
        Text(
            text = "Good Afternoon, $userName \uD83D\uDC4B",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = palette.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "What would you like to explore today?",
            style = MaterialTheme.typography.bodyMedium,
            color = palette.onSurfaceMuted
        )
    }
}

@Composable
private fun BlueMoonFeatureCard(onClick: () -> Unit) {
    val palette = NebularColors.current
    NebularCard(modifier = Modifier.fillMaxWidth().height(96.dp), onClick = onClick, backgroundColor = palette.surface) {
        Row(
            modifier = Modifier.fillMaxSize().padding(NebularDimension.spacerMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd)) {
                NebularLogoMark(size = 40.dp)
                Column {
                    Text(text = "Blue Moon", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = palette.onSurface)
                    Text(text = "Our most advanced AI model", style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceMuted)
                }
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Open", tint = palette.primary, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun FeatureCardItem(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    val palette = NebularColors.current
    NebularFeatureCard(
        icon = { Icon(imageVector = icon, contentDescription = title, tint = palette.primary, modifier = Modifier.size(NebularDimension.iconLg)) },
        title = title,
        description = description,
        onClick = onClick
    )
}

@Composable
private fun HomeChatInputBar(onSend: (String) -> Unit) {
    val palette = NebularColors.current
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(palette.surface, RoundedCornerShape(NebularDimension.radiusLg))
            .padding(NebularDimension.spacerXs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)
    ) {
        Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Attach", modifier = Modifier.size(22.dp), tint = palette.onSurfaceMuted)

        NebularTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = "Ask Nebular anything...",
            transparentBackground = true,
            modifier = Modifier.weight(1f)
        )

        Icon(imageVector = Icons.Default.Mic, contentDescription = "Voice", modifier = Modifier.size(22.dp), tint = palette.onSurfaceMuted)

        NebularIconButton(
            onClick = { if (message.isNotBlank()) { onSend(message); message = "" } },
            backgroundColor = palette.primary,
            size = 40.dp
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}
