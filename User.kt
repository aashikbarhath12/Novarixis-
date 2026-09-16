package com.novarixis.nebular.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImage: String? = null,
    val createdAt: Long,
    val planType: PlanType = PlanType.FREE,
    val onboardingCompleted: Boolean = false
)

@Serializable
enum class PlanType {
    @SerialName("free") FREE,
    @SerialName("pro") PRO,
    @SerialName("ultra") ULTRA
}

@Serializable
data class Subscription(
    val planType: PlanType,
    val priceInPaise: Int,
    val features: List<String>,
    val monthlyRequests: Int,
    val fileUploadLimitMb: Int,
    val priority: Boolean
)

val PlanSubscriptions = mapOf(
    PlanType.FREE to Subscription(
        planType = PlanType.FREE,
        priceInPaise = 0,
        features = listOf("Basic chat", "Limited models", "1 file per day"),
        monthlyRequests = 100,
        fileUploadLimitMb = 10,
        priority = false
    ),
    PlanType.PRO to Subscription(
        planType = PlanType.PRO,
        priceInPaise = 69_900,
        features = listOf("Advanced models", "Higher limits", "File upload & analysis", "Priority access"),
        monthlyRequests = 10_000,
        fileUploadLimitMb = 100,
        priority = true
    ),
    PlanType.ULTRA to Subscription(
        planType = PlanType.ULTRA,
        priceInPaise = 199_900,
        features = listOf("Everything in Pro", "Largest limits", "Early access to new features", "Premium support"),
        monthlyRequests = 100_000,
        fileUploadLimitMb = 500,
        priority = true
    )
)

@Serializable
data class AppSettings(
    val userId: String,
    val darkMode: Boolean = true,
    val language: String = "English",
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val memoryEnabled: Boolean = true,
    val selectedModelId: String = "blue-moon"
)
