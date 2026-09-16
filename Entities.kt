package com.novarixis.nebular.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val modelId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val archivedAt: Long? = null
)

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("conversationId")]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val role: String,
    val content: String,
    val timestamp: Long,
    val modelId: String? = null
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val profileImage: String? = null,
    val createdAt: Long,
    val planType: String = "free",
    val onboardingCompleted: Boolean = false
)

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String,
    val title: String,
    val content: String? = null,
    val filePath: String? = null,
    val createdAt: Long,
    val isFavorite: Boolean = false
)

@Entity(tableName = "memory")
data class MemoryEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val content: String,
    val type: String,
    val createdAt: Long
)

@Entity(tableName = "app_settings")
data class SettingsEntity(
    @PrimaryKey val userId: String,
    val darkMode: Boolean = true,
    val language: String = "English",
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val memoryEnabled: Boolean = true,
    val selectedModel: String = "blue-moon"
)
