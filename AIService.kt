package com.novarixis.nebular.domain.service

import com.novarixis.nebular.domain.model.ChatMessage
import com.novarixis.nebular.domain.model.ChatResponse
import kotlinx.coroutines.flow.Flow

interface AIService {
    suspend fun sendMessage(
        model: String,
        messages: List<ChatMessage>,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): Result<ChatResponse>

    fun streamMessage(
        model: String,
        messages: List<ChatMessage>,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): Flow<String>

    fun isModelSupported(modelId: String): Boolean
}

interface ChatRepository {
    suspend fun saveConversation(conversationId: String, modelId: String, messages: List<ChatMessage>): Result<Unit>
    suspend fun getMessages(conversationId: String): Result<List<ChatMessage>>
    suspend fun getAllConversations(): Result<List<Pair<String, String>>>
    suspend fun deleteConversation(conversationId: String): Result<Unit>
    suspend fun renameConversation(conversationId: String, newTitle: String): Result<Unit>
}

class AIServiceException(message: String, val code: Int? = null, cause: Throwable? = null) : Exception(message, cause)
class NetworkException(message: String) : Exception(message)
class AuthenticationException(message: String) : Exception(message)
class RateLimitException(message: String) : Exception(message)
