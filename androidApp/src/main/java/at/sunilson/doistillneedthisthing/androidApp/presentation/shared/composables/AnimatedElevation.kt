package at.sunilson.doistillneedthisthing.androidApp.presentation.shared.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
fun rememberScrollElevation(scrollState: ScrollState): State<Float> {
    return animateFloatAsState(
        if (scrollState.value != 0) 6f else 0f,
        tween(durationMillis = 300)
    )
}

@Composable
fun rememberScrollElevation(scrollState: LazyListState): State<Float> {
    return animateFloatAsState(
        if (scrollState.firstVisibleItemIndex != 0 || scrollState.firstVisibleItemScrollOffset != 0) 6f else 0f,
        tween(durationMillis = 300)
    )
}