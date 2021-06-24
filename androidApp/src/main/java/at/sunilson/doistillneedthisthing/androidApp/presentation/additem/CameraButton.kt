package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(widthDp = 100, heightDp = 100)
fun CameraButton(onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    var touched by remember { mutableStateOf(false) }
    val animatedScale: Float by animateFloatAsState(if (touched) 0.85f else 1f)
    Canvas(
        modifier
            .scale(animatedScale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onPress = {
                        touched = true
                        tryAwaitRelease()
                        touched = false
                    })
            }) {
        val canvasWidth = size.width
        val color = if (touched) Color.LightGray else Color.White

        drawCircle(color, canvasWidth / 2f - 30f)
        drawCircle(color, canvasWidth / 2f - 5f, style = Stroke(10f))
    }
}