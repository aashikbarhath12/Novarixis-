package com.novarixis.nebular.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.novarixis.nebular.data.db.entity.ConversationEntity
import com.novarixis.nebular.data.db.entity.MessageEntity
import com.novarixis.nebular.data.db.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversation(conversationId: String): ConversationEntity?

    @Query("SELECT * FROM conversations WHERE archivedAt IS NULL ORDER BY updatedAt DESC")
    fun getAllConversationsFlow(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE archivedAt IS NULL ORDER BY updatedAt DESC")
    suspend fun getAllConversations(): List<ConversationEntity>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessages(conversationId: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesFlow(conversationId: String): Flow<List<MessageEntity>>

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Query("UPDATE conversations SET title = :newTitle WHERE id = :conversationId")
    suspend fun updateConversationTitle(conversationId: String, newTitle: String)

    @Query("UPDATE conversations SET archivedAt = :archiveTime WHERE id = :conversationId")
    suspend fun archiveConversation(conversationId: String, archiveTime: Long)

    @Query("DELETE FROM conversations WHERE id = :conversationId")
    suspend fun deleteConversationById(conversationId: String)

    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesForConversation(conversationId: String)

    @Query("SELECT * FROM messages WHERE content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    suspend fun searchMessages(query: String): List<MessageEntity>
}

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Query("SELECT * FROM app_settings WHERE userId = :userId")
    suspend fun getSettings(userId: String): SettingsEntity?

    @Update
    suspend fun updateSettings(settings: SettingsEntity)

    @Query("UPDATE app_settings SET darkMode = :darkMode WHERE userId = :userId")
    suspend fun setDarkMode(userId: String, darkMode: Boolean)

    @Query("UPDATE app_settings SET selectedModel = :modelId WHERE userId = :userId")
    suspend fun setSelectedModel(userId: String, modelId: String)
}
