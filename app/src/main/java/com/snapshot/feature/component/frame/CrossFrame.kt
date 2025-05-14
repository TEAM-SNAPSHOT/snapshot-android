package com.snapshot.feature.component.frame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun CrossFrame(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .width(110.dp)
            .height(176.dp)
            .background(
                color = backgroundColor
            )
    )
}