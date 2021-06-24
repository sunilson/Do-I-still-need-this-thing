package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeState(val menuItem: HomeMenuItem = HomeMenuItem.Needed)
sealed class HomeSideEffects {
    object AddItem : HomeSideEffects()
    object Back : HomeSideEffects()
    object OpenSettings : HomeSideEffects()
}

sealed class HomeMenuItem {
    object Needed : HomeMenuItem()
    object NotNeeded : HomeMenuItem()
    object Removed : HomeMenuItem()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel(), ContainerHost<HomeState, HomeSideEffects> {

    override val container = container<HomeState, HomeSideEffects>(HomeState())

    fun addItemClicked() = intent {
        postSideEffect(HomeSideEffects.AddItem)
    }

    fun backClicked() = intent {
        postSideEffect(HomeSideEffects.Back)
    }

    fun menuItemSelected(menuItem: HomeMenuItem) = intent {
        reduce { state.copy(menuItem = menuItem) }
    }

    fun menuButtonClicked() = intent {
        postSideEffect(HomeSideEffects.OpenSettings)
    }
}