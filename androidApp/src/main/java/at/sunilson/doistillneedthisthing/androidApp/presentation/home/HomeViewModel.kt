package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

object HomeState
sealed class HomeSideEffects {
    object AddItem : HomeSideEffects()
    object Back : HomeSideEffects()
}

class HomeViewModel : ViewModel(), ContainerHost<HomeState, HomeSideEffects> {

    override val container = container<HomeState, HomeSideEffects>(HomeState)

    fun addItemClicked() = intent {
        postSideEffect(HomeSideEffects.AddItem)
    }

    fun backClicked() = intent {
        postSideEffect(HomeSideEffects.Back)
    }

}