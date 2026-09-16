package com.novarixis.nebular.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.novarixis.nebular.core.ui.components.NebularBackground
import com.novarixis.nebular.core.ui.components.NebularButton
import com.novarixis.nebular.core.ui.components.NebularButtonStyle
import com.novarixis.nebular.core.ui.graphics.NebularOrb
import com.novarixis.nebular.core.ui.theme.NebularColors
import com.novarixis.nebular.core.ui.theme.NebularDimension
import kotlinx.coroutines.launch

private data class OnboardingPageData(val title: String, val description: String)

private val pages = listOf(
    OnboardingPageData(
        "Welcome to Nebular",
        "Your AI companion to chat, create, learn and explore without limits."
    ),
    OnboardingPageData(
        "Smart Conversations",
        "Ask anything and get instant, intelligent responses from Blue Moon and other advanced models."
    ),
    OnboardingPageData(
        "Create & Explore",
        "Generate images, write documents, analyze files, and explore the web — all in one place."
    ),
    OnboardingPageData(
        "Your AI, Your Way",
        "Switch models, customize themes, and shape Nebular around how you work best."
    )
)

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val palette = NebularColors.current

    NebularBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(NebularDimension.spacerMd), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.labelLarge,
                    color = palette.onSurfaceMuted,
                    modifier = Modifier.clickable(onClick = onComplete).padding(8.dp)
                )
            }

            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPage(pages[page])
            }

            Column(modifier = Modifier.fillMaxWidth().padding(NebularDimension.spacerLg)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    pages.indices.forEach { index ->
                        val selected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (selected) 10.dp else 7.dp)
                                .background(if (selected) palette.primary else palette.onSurfaceMuted.copy(alpha = 0.4f), CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(NebularDimension.spacerXl))

                if (pagerState.currentPage < pages.lastIndex) {
                    NebularButton(
                        text = "Next",
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        style = NebularButtonStyle.PRIMARY,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    NebularButton(
                        text = "Get Started",
                        onClick = onComplete,
                        style = NebularButtonStyle.PRIMARY,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(data: OnboardingPageData) {
    val palette = NebularColors.current
    Column(
        modifier = Modifier.fillMaxSize().padding(NebularDimension.spacerXl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NebularOrb(size = 190.dp)

        Spacer(modifier = Modifier.height(NebularDimension.spacer2xl))

        Text(
            text = data.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = palette.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(NebularDimension.spacerSm))

        Text(
            text = data.description,
            style = MaterialTheme.typography.bodyLarge,
            color = palette.onSurfaceMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().width(280.dp)
        )
    }
}
