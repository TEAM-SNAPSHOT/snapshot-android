package com.snapshot.feature.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.snapshot.res.modifier.ColorTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit
) {
    LaunchedEffect(true) {
        delay(800)
        navigateToHome()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorTheme.colors.bg)
    ) {

    }
}