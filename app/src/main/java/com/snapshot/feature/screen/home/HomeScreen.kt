package com.snapshot.feature.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.snapshot.feature.component.TopBar
import com.snapshot.ui.theme.BG_LIGHT

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BG_LIGHT)
    ) {
    TopBar()
    }
}