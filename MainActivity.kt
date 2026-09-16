package com.novarixis.nebular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.novarixis.nebular.core.ui.theme.NebularTheme
import com.novarixis.nebular.di.dataStore
import com.novarixis.nebular.feature.chat.ChatScreen
import com.novarixis.nebular.feature.home.HomeScreen
import com.novarixis.nebular.feature.onboarding.OnboardingScreen
import com.novarixis.nebular.feature.settings.SettingsScreen
import com.novarixis.nebular.feature.settings.SettingsKeys
import com.novarixis.nebular.feature.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setContent {
            val context = LocalContext.current
            val systemDark = isSystemInDarkTheme()
            val darkModePref by context.dataStore.data
                .map { prefs -> prefs[SettingsKeys.DARK_MODE] ?: systemDark }
                .collectAsState(initial = systemDark)

            NebularTheme(darkTheme = darkModePref) {
                NebularApp()
            }
        }
    }
}

@Composable
fun NebularApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                },
                onNavigateToOnboarding = {
                    navController.navigate("onboarding") { popUpTo("splash") { inclusive = true } }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("home") { popUpTo("onboarding") { inclusive = true } }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToChat = { conversationId -> navController.navigate("chat/$conversationId") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }

        composable(
            route = "chat/{conversationId}",
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) {
            ChatScreen(
                conversationId = it.arguments?.getString("conversationId") ?: "new",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
