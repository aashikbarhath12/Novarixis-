package com.novarixis.nebular.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.novarixis.nebular.data.db.NebularDatabase
import com.novarixis.nebular.data.db.dao.ConversationDao
import com.novarixis.nebular.data.db.dao.SettingsDao
import com.novarixis.nebular.data.network.ApiClient
import com.novarixis.nebular.data.network.NebularApiService
import com.novarixis.nebular.data.service.AIServiceImpl
import com.novarixis.nebular.data.service.ChatRepositoryImpl
import com.novarixis.nebular.domain.service.AIService
import com.novarixis.nebular.domain.service.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES = "nebular_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NebularDatabase =
        NebularDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideConversationDao(database: NebularDatabase): ConversationDao = database.conversationDao()

    @Singleton
    @Provides
    fun provideSettingsDao(database: NebularDatabase): SettingsDao = database.settingsDao()

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Singleton
    @Provides
    fun provideNebularApiService(): NebularApiService = ApiClient.createApiService()

    @Singleton
    @Provides
    fun provideAIService(apiService: NebularApiService): AIService = AIServiceImpl(apiService)

    @Singleton
    @Provides
    fun provideChatRepository(conversationDao: ConversationDao): ChatRepository =
        ChatRepositoryImpl(conversationDao)
}
