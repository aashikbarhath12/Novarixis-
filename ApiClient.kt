package com.novarixis.nebular.data.network

import com.novarixis.nebular.domain.model.ChatRequest
import com.novarixis.nebular.domain.model.ChatResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface NebularApiService {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest,
        @Header("Authorization") token: String
    ): ChatResponse
}

/**
 * Secure key storage placeholder. In production, back this with
 * EncryptedSharedPreferences or the Android Keystore — never hardcode
 * a real API key in source.
 */
object SecureKeyManager {
    @Volatile private var apiKey: String? = null
    fun setApiKey(key: String) { apiKey = key }
    fun getApiKey(): String? = apiKey
    fun clearApiKey() { apiKey = null }
}

object ApiClient {
    private const val BASE_URL = "https://api.example.com/"
    private const val TIMEOUT_SECONDS = 30L

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }

    fun createApiService(): NebularApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NebularApiService::class.java)
    }
}
