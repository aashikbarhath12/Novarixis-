package com.novarixis.nebular.feature.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novarixis.nebular.domain.model.AvailableModel
import com.novarixis.nebular.domain.model.ChatMessage
import com.novarixis.nebular.domain.model.MessageRole
import com.novarixis.nebular.domain.service.AIService
import com.novarixis.nebular.domain.service.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val conversationId: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedModel: AvailableModel = AvailableModel.BlueMoon
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val aiService: AIService,
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val conversationId: String =
        savedStateHandle.get<String>("conversationId")?.takeIf { it.isNotBlank() && it != "new" }
            ?: UUID.randomUUID().toString()

    private val _uiState = MutableStateFlow(ChatUiState(conversationId = conversationId))
    val uiState: StateFlow<ChatUiState> = _uiState

    init {
        loadConversation()
    }

    private fun loadConversation() {
        viewModelScope.launch {
            chatRepository.getMessages(conversationId).onSuccess { messages ->
                if (messages.isNotEmpty()) {
                    _uiState.update { it.copy(messages = messages) }
                }
            }
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = MessageRole.USER,
                content = content,
                timestamp = System.currentTimeMillis(),
                modelId = _uiState.value.selectedModel.model.id
            )

            _uiState.update { it.copy(messages = it.messages + userMessage, isLoading = true, error = null) }

            val assistantId = UUID.randomUUID().toString()
            val placeholder = ChatMessage(
                id = assistantId,
                role = MessageRole.ASSISTANT,
                content = "",
                timestamp = System.currentTimeMillis(),
                modelId = _uiState.value.selectedModel.model.id
            )
            _uiState.update { it.copy(messages = it.messages + placeholder) }

            try {
                val accumulated = StringBuilder()
                aiService.streamMessage(
                    model = _uiState.value.selectedModel.model.id,
                    messages = _uiState.value.messages.filter { it.id != assistantId }
                ).collect { token ->
                    accumulated.append(token)
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages.map { msg ->
                                if (msg.id == assistantId) msg.copy(content = accumulated.toString()) else msg
                            }
                        )
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
                chatRepository.saveConversation(conversationId, _uiState.value.selectedModel.model.id, _uiState.value.messages)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Something went wrong") }
            }
        }
    }

    fun selectModel(modelId: String) {
        _uiState.update { it.copy(selectedModel = AvailableModel.fromId(modelId)) }
    }
}
