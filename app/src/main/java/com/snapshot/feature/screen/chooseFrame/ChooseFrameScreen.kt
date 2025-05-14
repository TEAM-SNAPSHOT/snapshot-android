package com.snapshot.feature.screen.chooseFrame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snapshot.feature.component.bottomBar.BottomNavigationBar
import com.snapshot.feature.component.frame.CrossFrame
import com.snapshot.res.modifier.ColorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseFrameScreen(
    modifier: Modifier = Modifier,
    navigateToPhoto: () -> Unit
) {
    LaunchedEffect(true) {
        navigateToPhoto()
    }
}