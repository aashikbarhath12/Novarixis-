package com.novarixis.nebular.feature.chat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novarixis.nebular.core.ui.components.ChatMessageBubble
import com.novarixis.nebular.core.ui.components.ChatResponseActions
import com.novarixis.nebular.core.ui.components.NebularBackground
import com.novarixis.nebular.core.ui.components.NebularIconButton
import com.novarixis.nebular.core.ui.components.NebularTextField
import com.novarixis.nebular.core.ui.components.SuggestedPromptChip
import com.novarixis.nebular.core.ui.components.TypingIndicator
import com.novarixis.nebular.core.ui.graphics.ModelGlowIcon
import com.novarixis.nebular.core.ui.graphics.NebularLogoMark
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension
import com.novarixis.nebular.domain.model.AvailableModel
import com.novarixis.nebular.domain.model.MessageRole
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    conversationId: String,
    viewModel: ChatViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var messageText by remember { mutableStateOf("") }
    var showModelSelector by remember { mutableStateOf(false) }
    val palette = NebularColors.current
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val scope = rememberCoroutineScope()

    NebularBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            ChatTopBar(
                selectedModel = uiState.selectedModel,
                onBackClick = onBackClick,
                onModelClick = { showModelSelector = true }
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(NebularDimension.spacerMd),
                verticalArrangement = Arrangement.spacedBy(NebularDimension.spacerMd)
            ) {
                if (uiState.messages.isEmpty()) {
                    item { EmptyChatState() }
                }

                items(uiState.messages, key = { it.id }) { message ->
                    Column(verticalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
                        ChatMessageBubble(message = message)
                        if (message.role == MessageRole.ASSISTANT) {
                            ChatResponseActions()
                            Row(horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerXs)) {
                                SuggestedPromptChip("Explain with diagram", onClick = {})
                                SuggestedPromptChip("Real-world applications", onClick = {})
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    item { TypingIndicator() }
                }
            }

            ChatComposer(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(messageText)
                        messageText = ""
                        scope.launch {
                            if (uiState.messages.isNotEmpty()) listState.animateScrollToItem(uiState.messages.size)
                        }
                    }
                }
            )
        }

        if (showModelSelector) {
            ModelSelectorSheet(
                selectedModelId = uiState.selectedModel.model.id,
                onDismiss = { showModelSelector = false },
                onModelSelected = { modelId ->
                    viewModel.selectModel(modelId)
                    showModelSelector = false
                }
            )
        }
    }
}

@Composable
private fun ChatTopBar(
    selectedModel: AvailableModel,
    onBackClick: () -> Unit,
    onModelClick: () -> Unit
) {
    val palette = NebularColors.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(NebularDimension.spacerMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(22.dp).clickable(onClick = onBackClick),
                tint = palette.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm),
                modifier = Modifier.clickable(onClick = onModelClick)
            ) {
                NebularLogoMark(size = 34.dp, glow = false)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = selectedModel.model.displayName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = "Choose model", tint = palette.onSurfaceMuted, modifier = Modifier.size(18.dp))
                    }
                    Text(text = "Advanced AI Model", style = MaterialTheme.typography.labelSmall, color = palette.onSurfaceMuted)
                }
            }
        }

        Icon(imageVector = Icons.Default.Add, contentDescription = "New chat", modifier = Modifier.size(22.dp), tint = palette.onSurface)
    }
}

@Composable
private fun EmptyChatState() {
    val palette = NebularColors.current
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NebularLogoMark(size = 64.dp)
        Spacer(modifier = Modifier.height(NebularDimension.spacerMd))
        Text(text = "Ask Blue Moon anything", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = palette.onSurface)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Start typing below to begin the conversation", style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceMuted)
    }
}

@Composable
private fun ChatComposer(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val palette = NebularColors.current
    Box(modifier = Modifier.fillMaxWidth().padding(NebularDimension.spacerMd)) {
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
                value = messageText,
                onValueChange = onMessageChange,
                placeholder = "Ask Nebular anything...",
                transparentBackground = true,
                singleLine = false,
                modifier = Modifier.weight(1f)
            )

            Icon(imageVector = Icons.Default.Mic, contentDescription = "Voice", modifier = Modifier.size(22.dp), tint = palette.onSurfaceMuted)

            NebularIconButton(onClick = onSend, backgroundColor = palette.primary, size = 40.dp) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun ModelSelectorSheet(
    selectedModelId: String,
    onDismiss: () -> Unit,
    onModelSelected: (String) -> Unit
) {
    val palette = NebularColors.current
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)).clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(palette.surface, RoundedCornerShape(topStart = NebularDimension.radiusXl, topEnd = NebularDimension.radiusXl))
                .padding(NebularDimension.spacerMd)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Choose a Model", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = palette.onSurface)
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(24.dp).clickable(onClick = onDismiss),
                    tint = palette.onSurfaceMuted
                )
            }

            Spacer(modifier = Modifier.height(NebularDimension.spacerMd))

            Column(verticalArrangement = Arrangement.spacedBy(NebularDimension.spacerSm)) {
                AvailableModel.ALL.forEach { model ->
                    com.novarixis.nebular.core.ui.components.NebularModelCard(
                        icon = { ModelGlowIcon(color = modelColorFor(model), size = 36.dp) },
                        name = model.model.displayName,
                        description = model.model.description,
                        category = model.model.category,
                        isSelected = model.model.id == selectedModelId,
                        onClick = { onModelSelected(model.model.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(NebularDimension.spacerMd))
        }
    }
}

private fun modelColorFor(model: AvailableModel): Color = when (model) {
    is AvailableModel.BlueMoon -> com.novarixis.nebular.core.ui.theme.ModelColors.BlueMoon
    is AvailableModel.StarPro -> com.novarixis.nebular.core.ui.theme.ModelColors.StarPro
    is AvailableModel.NovaVision -> com.novarixis.nebular.core.ui.theme.ModelColors.NovaVision
    is AvailableModel.StellerPlus -> com.novarixis.nebular.core.ui.theme.ModelColors.StellerPlus
    is AvailableModel.OrionCode -> com.novarixis.nebular.core.ui.theme.ModelColors.OrionCode
}
