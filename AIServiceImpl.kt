package com.novarixis.nebular.data.service

import com.novarixis.nebular.data.db.dao.ConversationDao
import com.novarixis.nebular.data.db.entity.ConversationEntity
import com.novarixis.nebular.data.db.entity.MessageEntity
import com.novarixis.nebular.data.network.NebularApiService
import com.novarixis.nebular.data.network.SecureKeyManager
import com.novarixis.nebular.domain.model.AvailableModel
import com.novarixis.nebular.domain.model.ChatMessage
import com.novarixis.nebular.domain.model.ChatRequest
import com.novarixis.nebular.domain.model.ChatResponse
import com.novarixis.nebular.domain.model.MessageRole
import com.novarixis.nebular.domain.service.AIService
import com.novarixis.nebular.domain.service.AIServiceException
import com.novarixis.nebular.domain.service.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * Talks to the configured backend when an API key is present; otherwise
 * falls back to a local simulated response so the app is fully demoable
 * without a live backend. Swap [SecureKeyManager] wiring for your real
 * key-management flow before shipping.
 */
class AIServiceImpl(
    private val apiService: NebularApiService
) : AIService {

    override suspend fun sendMessage(
        model: String,
        messages: List<ChatMessage>,
        temperature: Float,
        maxTokens: Int
    ): Result<ChatResponse> {
        val apiKey = SecureKeyManager.getApiKey()
        if (apiKey.isNullOrBlank()) {
            return Result.failure(AIServiceException("No API key configured — using demo mode for streaming instead."))
        }
        return try {
            val request = ChatRequest(
                model = model,
                messages = messages.map { mapOf("role" to it.role.name.lowercase(), "content" to it.content) },
                temperature = temperature,
                maxTokens = maxTokens,
                stream = false
            )
            Result.success(apiService.sendMessage(request, "Bearer $apiKey"))
        } catch (e: Exception) {
            Timber.e(e, "sendMessage failed")
            Result.failure(AIServiceException("Failed to send message: ${e.message}", cause = e))
        }
    }

    override fun streamMessage(
        model: String,
        messages: List<ChatMessage>,
        temperature: Float,
        maxTokens: Int
    ): Flow<String> = flow {
        val apiKey = SecureKeyManager.getApiKey()
        if (apiKey.isNullOrBlank()) {
            // Demo-mode simulated streaming so the UI is fully testable offline.
            val demoResponse = buildDemoResponse(messages.lastOrNull()?.content.orEmpty(), model)
            for (word in demoResponse.split(" ")) {
                emit("$word ")
                delay(35)
            }
            return@flow
        }

        // Real backend path — replace with SSE/WebSocket streaming client.
        try {
            val result = sendMessage(model, messages, temperature, maxTokens)
            result.onSuccess { response ->
                val content = response.choices.firstOrNull()?.message?.content.orEmpty()
                for (word in content.split(" ")) {
                    emit("$word ")
                    delay(20)
                }
            }.onFailure { throw it }
        } catch (e: Exception) {
            Timber.e(e, "streamMessage failed")
            throw e
        }
    }

    override fun isModelSupported(modelId: String): Boolean =
        AvailableModel.ALL.any { it.model.id == modelId }

    private fun buildDemoResponse(prompt: String, modelId: String): String {
        val modelName = AvailableModel.fromId(modelId).model.displayName
        return "Thanks for your message! I'm $modelName, running in demo mode since no API key " +
            "is configured yet. Once you connect a real backend in ApiClient.kt and set a key via " +
            "SecureKeyManager, I'll respond using the live model instead. You asked about: " +
            "\"${prompt.take(80)}\" — in production this is where the real answer would stream in."
    }
}

class ChatRepositoryImpl(
    private val conversationDao: ConversationDao
) : ChatRepository {

    override suspend fun saveConversation(
        conversationId: String,
        modelId: String,
        messages: List<ChatMessage>
    ): Result<Unit> {
        return try {
            val existing = conversationDao.getConversation(conversationId)
            val now = System.currentTimeMillis()
            val conversation = ConversationEntity(
                id = conversationId,
                title = messages.firstOrNull { it.role == MessageRole.USER }?.content?.take(50) ?: "New conversation",
                modelId = modelId,
                createdAt = existing?.createdAt ?: now,
                updatedAt = now
            )
            conversationDao.insertConversation(conversation)

            val entities = messages.map {
                MessageEntity(
                    id = it.id,
                    conversationId = conversationId,
                    role = it.role.name.lowercase(),
                    content = it.content,
                    timestamp = it.timestamp,
                    modelId = it.modelId
                )
            }
            conversationDao.insertMessages(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "saveConversation failed")
            Result.failure(e)
        }
    }

    override suspend fun getMessages(conversationId: String): Result<List<ChatMessage>> {
        return try {
            val messages = conversationDao.getMessages(conversationId).map { entity ->
                ChatMessage(
                    id = entity.id,
                    role = when (entity.role) {
                        "user" -> MessageRole.USER
                        "assistant" -> MessageRole.ASSISTANT
                        else -> MessageRole.SYSTEM
                    },
                    content = entity.content,
                    timestamp = entity.timestamp,
                    modelId = entity.modelId
                )
            }
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllConversations(): Result<List<Pair<String, String>>> {
        return try {
            Result.success(conversationDao.getAllConversations().map { it.id to it.title })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteConversation(conversationId: String): Result<Unit> {
        return try {
            conversationDao.deleteMessagesForConversation(conversationId)
            conversationDao.deleteConversationById(conversationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun renameConversation(conversationId: String, newTitle: String): Result<Unit> {
        return try {
            conversationDao.updateConversationTitle(conversationId, newTitle)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
