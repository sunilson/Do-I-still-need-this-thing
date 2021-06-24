package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import at.sunilson.doistillneedthisthing.androidApp.domain.CropBitmapAndSaveToFile
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.composables.AnimatedProgressOverlay

@ExperimentalAnimationApi
@Composable
fun AddItemOverlay(viewModel: AddItemViewModel = viewModel()) {
    val state by viewModel.container.stateFlow.collectAsState(initial = AddItemState.Initial)
    ConstraintLayout(Modifier) {
        val (cameraButton, flashButton, detectionButton) = createRefs()
        CameraButton(
            { viewModel.captureImageButtonClicked() },
            Modifier
                .size(80.dp)
                .constrainAs(cameraButton) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        FloatingActionButton(
            onClick = { viewModel.torchButtonClicked() },
            Modifier
                .size(50.dp)
                .constrainAs(flashButton) {
                    top.linkTo(cameraButton.top)
                    bottom.linkTo(cameraButton.bottom)
                    start.linkTo(parent.start, margin = 20.dp)
                }) {
            if ((state as? AddItemState.Camera)?.torchEnabled == true) {
                Icon(Icons.Rounded.FlashOff, contentDescription = "")
            } else {
                Icon(Icons.Rounded.FlashOn, contentDescription = "")
            }
        }

        FloatingActionButton(
            onClick = { viewModel.toggleObjectDetectionClicked() },
            Modifier
                .size(50.dp)
                .constrainAs(detectionButton) {
                    top.linkTo(cameraButton.top)
                    bottom.linkTo(cameraButton.bottom)
                    end.linkTo(parent.end, margin = 20.dp)
                }) {
            if ((state as? AddItemState.Camera)?.objectDetectionEnabled == true) {
                Icon(Icons.Rounded.VisibilityOff, contentDescription = "")
            } else {
                Icon(Icons.Rounded.RemoveRedEye, contentDescription = "")
            }
        }
    }

    AnimatedProgressOverlay(show = (state as? AddItemState.Camera)?.processingImage ?: false)
}

@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0x989a82)
@Composable
private fun preview() {
    AddItemOverlay(AddItemViewModel(CropBitmapAndSaveToFile(LocalContext.current)))
}