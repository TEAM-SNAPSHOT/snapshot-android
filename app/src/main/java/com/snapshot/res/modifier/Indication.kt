package com.snapshot.res.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

fun Modifier.pressEffect(
    scaleDown: Float = 0.95f,
    darkenColor: Color = Color.Black,
    darkenAlpha: Float = 0.1f,
    animationDuration: Int = 100,
    onClick: suspend () -> Unit,
    enable: Boolean = true
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "pressScale"
    )
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isPressed) darkenAlpha else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "pressAlpha"
    )

    if (enable) {
        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawWithContent {
                drawContent()
                if (overlayAlpha > 0f) {
                    drawRect(
                        color = darkenColor,
                        alpha = overlayAlpha,
                    )
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    coroutineScope.launch {
                        onClick()
                    }
                }
            )
    } else {
        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawWithContent {
                drawContent()
                if (overlayAlpha > 0f) {
                    drawRect(
                        color = darkenColor,
                        alpha = overlayAlpha,
                    )
                }
            }
    }
}

