package com.novarixis.nebular.feature.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novarixis.nebular.core.ui.components.NebularBackground
import com.novarixis.nebular.core.ui.graphics.NebularOrb
import com.novarixis.nebular.core.ui.theme.NebularColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onboardingCompleted: Boolean = false
) {
    var showOrb by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    val palette = NebularColors.current

    LaunchedEffect(Unit) {
        delay(150)
        showOrb = true
        delay(350)
        showTitle = true
        delay(400)
        showTagline = true
        delay(900)
        if (onboardingCompleted) onNavigateToHome() else onNavigateToOnboarding()
    }

    NebularBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = showOrb, enter = fadeIn() + scaleIn(initialScale = 0.7f)) {
                NebularOrb(size = 140.dp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(visible = showTitle, enter = fadeIn() + slideInVertically { it / 3 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nebular",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = palette.onBackground,
                        fontSize = 42.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Powered by Blue Moon",
                        style = MaterialTheme.typography.titleMedium,
                        color = palette.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(visible = showTagline, enter = fadeIn() + slideInVertically { it / 2 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "A Brighter Mind",
                        style = MaterialTheme.typography.bodyLarge,
                        color = palette.onSurfaceMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "for a Bigger Tomorrow",
                        style = MaterialTheme.typography.bodyLarge,
                        color = palette.onSurfaceMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(visible = showTagline, enter = fadeIn()) {
                CircularProgressIndicator(modifier = Modifier.height(28.dp), color = palette.primary, strokeWidth = 2.5.dp)
            }
        }
    }
}
