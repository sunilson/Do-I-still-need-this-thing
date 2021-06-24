package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import androidx.core.graphics.toRect
import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.androidApp.domain.CropBitmapAndSaveToFile
import at.sunilson.doistillneedthisthing.androidApp.domain.entities.CropBitmapParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class AddItemState {
    object Initial : AddItemState()

    data class Camera(
        val objectDetectionEnabled: Boolean = false,
        val processingImage: Boolean = false,
        val torchEnabled: Boolean = false
    ) : AddItemState()

    object PermissionMissing : AddItemState()
}

sealed class AddItemSideEffects {
    object Back : AddItemSideEffects()
    object TakeImage : AddItemSideEffects()
    data class ImageTaken(val uri: Uri) : AddItemSideEffects()
}

@HiltViewModel
class AddItemViewModel @Inject constructor(private val cropBitmapAndSaveToFile: CropBitmapAndSaveToFile) :
    ViewModel(),
    ContainerHost<AddItemState, AddItemSideEffects> {

    override val container = container<AddItemState, AddItemSideEffects>(AddItemState.Initial)

    fun viewCreated() = intent { reduce { AddItemState.Initial } }

    fun permissionsGranted() = intent { reduce { AddItemState.Camera() } }

    fun permissionsDenied() = intent { reduce { AddItemState.PermissionMissing } }

    fun backClicked() = intent { postSideEffect(AddItemSideEffects.Back) }

    fun detectedObjectSelected(currentCameraBitmap: Bitmap?, rotation: Int, selectedRect: RectF) =
        intent {
            val currentState = state
            if (currentState is AddItemState.Camera && currentState.processingImage) return@intent
            if (currentCameraBitmap == null) return@intent

            reduce {
                AddItemState.Camera(
                    (state as AddItemState.Camera).objectDetectionEnabled,
                    true
                )
            }

            val (uri, error) = cropBitmapAndSaveToFile(
                CropBitmapParameters(
                    currentCameraBitmap,
                    rotation,
                    selectedRect.toRect()
                )
            )

            if (uri != null) {
                postSideEffect(AddItemSideEffects.ImageTaken(uri))
            }

            reduce {
                AddItemState.Camera(
                    (state as AddItemState.Camera).objectDetectionEnabled,
                    false
                )
            }
        }

    fun captureImageButtonClicked() = intent {
        postSideEffect(AddItemSideEffects.TakeImage)
        reduce {
            AddItemState.Camera(
                (state as AddItemState.Camera).objectDetectionEnabled,
                true
            )
        }
    }

    fun imageCaptured(uri: Uri) = intent {
        postSideEffect(AddItemSideEffects.ImageTaken(uri))
        reduce {
            AddItemState.Camera(
                (state as AddItemState.Camera).objectDetectionEnabled,
                false
            )
        }
    }

    fun torchButtonClicked() = intent {
        val state = state as AddItemState.Camera
        reduce { state.copy(torchEnabled = !state.torchEnabled) }
    }

    fun toggleObjectDetectionClicked() = intent {
        val currentState = state
        if (currentState is AddItemState.Camera) {
            reduce { AddItemState.Camera(!currentState.objectDetectionEnabled) }
        }
    }
}