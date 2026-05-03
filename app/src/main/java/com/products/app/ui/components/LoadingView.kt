package com.products.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.products.app.ui.theme.ProductAppTheme

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = LoadingConstants.InfiniteLabel)
    val alpha by transition.animateFloat(
        initialValue = LoadingConstants.PulseMinAlpha,
        targetValue = LoadingConstants.PulseMaxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = LoadingConstants.PulseDurationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = LoadingConstants.AlphaLabel,
    )
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.alpha(alpha),
        )
    }
}

private object LoadingConstants {
    const val InfiniteLabel = "loading_pulse"
    const val AlphaLabel = "loading_alpha"
    const val PulseMinAlpha = 0.35f
    const val PulseMaxAlpha = 1f
    const val PulseDurationMillis = 900
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
private fun LoadingViewPreview() {
    ProductAppTheme {
        LoadingView()
    }
}
