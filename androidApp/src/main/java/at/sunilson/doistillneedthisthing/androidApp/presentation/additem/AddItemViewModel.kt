package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

sealed class AddItemState {
    object Initial : AddItemState()
    object Camera : AddItemState()
    object PermissionMissing : AddItemState()
}

sealed class AddItemSideEffects {
    object AskForPermissions : AddItemSideEffects()
    object ShowRationale : AddItemSideEffects()
    object Back : AddItemSideEffects()
}

class AddItemViewModel : ViewModel(), ContainerHost<AddItemState, AddItemSideEffects> {

    override val container = container<AddItemState, AddItemSideEffects>(AddItemState.Initial)

    fun viewCreated() = intent {
        reduce { AddItemState.Initial }
        postSideEffect(AddItemSideEffects.AskForPermissions)
    }

    fun permissionsGranted() = intent { reduce { AddItemState.Camera } }

    fun rationaleAccepted() = intent { postSideEffect(AddItemSideEffects.AskForPermissions) }

    fun rationaleDenied() = intent { reduce { AddItemState.PermissionMissing } }

    fun permissionsDenied(showRationale: Boolean) = intent {
        if (showRationale) {
            postSideEffect(AddItemSideEffects.ShowRationale)
        } else {
            reduce { AddItemState.PermissionMissing }
        }
    }

    fun backClicked() = intent { postSideEffect(AddItemSideEffects.Back) }

}