package at.sunilson.doistillneedthisthing.androidApp.presentation.setitemdetails

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.androidApp.domain.ClassifyImage
import at.sunilson.doistillneedthisthing.androidApp.domain.RotateBitmapFromExifData
import at.sunilson.doistillneedthisthing.shared.domain.AddItem
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.*
import javax.inject.Inject

data class SetItemDetailsState(
    val processedBitmap: Bitmap? = null,
    val classifiedLabel: String? = null
)

sealed class SetItemDetailsSideEffects {
    object ItemAdded : SetItemDetailsSideEffects()
    data class ItemAddingFailed(val error: Throwable?) : SetItemDetailsSideEffects()
}

@HiltViewModel
class SetItemDetailsViewModel @Inject constructor(
    private val rotateBitmapFromExifData: RotateBitmapFromExifData,
    private val classifyImage: ClassifyImage,
    private val addItem: AddItem
) : ViewModel(), ContainerHost<SetItemDetailsState, SetItemDetailsSideEffects> {
    override val container = container<SetItemDetailsState, SetItemDetailsSideEffects>(
        SetItemDetailsState()
    )

    fun imageReceived(uri: Uri?) = intent {
        if (uri == null) {
            // TODO
            return@intent
        }

        rotateBitmapFromExifData(uri).fold(
            {
                reduce { state.copy(processedBitmap = it) }
                classifyLoadedImage()
            },
            {
                // TODO
            }
        )
    }

    private fun classifyLoadedImage() = intent {
        classifyImage(state.processedBitmap ?: return@intent).onSuccess {
            reduce { state.copy(classifiedLabel = it?.replaceFirstChar { it.toUpperCase() }) }
        }
    }

    fun addItemClicked(title: String, imagePath: Uri, location: String?) = intent {
        addItem(
            Item(
                title,
                imagePath.toString(),
                System.currentTimeMillis(),
                location = location
            )
        ).fold(
            { postSideEffect(SetItemDetailsSideEffects.ItemAdded) },
            { postSideEffect(SetItemDetailsSideEffects.ItemAddingFailed(it)) }
        )
    }
}