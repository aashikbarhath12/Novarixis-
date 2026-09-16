package com.novarixis.nebular.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AIModel(
    val id: String,
    val displayName: String,
    val description: String,
    val category: String,
    val capabilities: List<String>,
    val contextLength: Int,
    val supportsVision: Boolean,
    val supportsTools: Boolean,
    val supportsCode: Boolean,
)

/** Type-safe registry of models — matches the reference "Choose a Model" sheet exactly. */
sealed class AvailableModel(val model: AIModel) {
    object BlueMoon : AvailableModel(
        AIModel(
            id = "blue-moon",
            displayName = "Blue Moon",
            description = "Our most advanced model",
            category = "Best for everything",
            capabilities = listOf("Chat", "Vision", "Code", "Analysis", "Tools"),
            contextLength = 128_000,
            supportsVision = true,
            supportsTools = true,
            supportsCode = true
        )
    )

    object StarPro : AvailableModel(
        AIModel(
            id = "star-pro",
            displayName = "Star Pro",
            description = "Fast & efficient",
            category = "Speed optimized",
            capabilities = listOf("Chat", "Quick analysis", "Summarization"),
            contextLength = 32_000,
            supportsVision = false,
            supportsTools = true,
            supportsCode = false
        )
    )

    object NovaVision : AvailableModel(
        AIModel(
            id = "nova-vision",
            displayName = "Nova Vision",
            description = "Image & vision understanding",
            category = "Vision specialist",
            capabilities = listOf("Image analysis", "OCR", "Diagrams"),
            contextLength = 64_000,
            supportsVision = true,
            supportsTools = false,
            supportsCode = false
        )
    )

    object StellerPlus : AvailableModel(
        AIModel(
            id = "steller-plus",
            displayName = "Steller Plus",
            description = "Research & analysis",
            category = "Research focused",
            capabilities = listOf("Research", "Citations", "Data processing"),
            contextLength = 200_000,
            supportsVision = false,
            supportsTools = true,
            supportsCode = true
        )
    )

    object OrionCode : AvailableModel(
        AIModel(
            id = "orion-code",
            displayName = "Orion Code",
            description = "Optimized for coding",
            category = "Code specialist",
            capabilities = listOf("Codegen", "Debugging", "Optimization"),
            contextLength = 100_000,
            supportsVision = false,
            supportsTools = true,
            supportsCode = true
        )
    )

    companion object {
        val ALL = listOf(BlueMoon, StarPro, NovaVision, StellerPlus, OrionCode)
        fun fromId(id: String): AvailableModel = ALL.find { it.model.id == id } ?: BlueMoon
    }
}

@Serializable
enum class MessageRole {
    @SerialName("user") USER,
    @SerialName("assistant") ASSISTANT,
    @SerialName("system") SYSTEM
}

@Serializable
data class ChatMessage(
    val id: String,
    val role: MessageRole,
    val content: String,
    val timestamp: Long,
    val modelId: String? = null,
)

@Serializable
data class Conversation(
    val id: String,
    val title: String,
    val modelId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val archivedAt: Long? = null
)

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<Map<String, String>>,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 2048,
    val stream: Boolean = false
)

@Serializable
data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage? = null
)

@Serializable
data class Choice(val message: ApiMessage, val finishReason: String? = null)

@Serializable
data class ApiMessage(val role: String, val content: String)

@Serializable
data class Usage(val promptTokens: Int, val completionTokens: Int, val totalTokens: Int)
