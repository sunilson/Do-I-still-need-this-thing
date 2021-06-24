package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.androidApp.presentation.home.HomeSideEffects
import at.sunilson.doistillneedthisthing.shared.domain.GetSettings
import at.sunilson.doistillneedthisthing.shared.domain.UpdateSettings
import at.sunilson.doistillneedthisthing.shared.domain.entities.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class SettingsSideEffects {
    object OpenNotificationSettings : SettingsSideEffects()
    object ChangeLanguage : SettingsSideEffects()
    object Back: SettingsSideEffects()
}

data class SettingsState(val settings: Settings? = null)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettings: GetSettings,
    private val updateSettings: UpdateSettings
) : ViewModel(),
    ContainerHost<SettingsState, SettingsSideEffects> {
    override val container = container<SettingsState, SettingsSideEffects>(SettingsState())

    init {
        intent {
            val settings = getSettings(Unit).first()
            reduce { state.copy(settings = settings) }
        }
    }

    fun notificationsEnabledToggled(checked: Boolean) = intent {
        val newSettings = state.settings?.copy(notificationsEnabled = checked) ?: return@intent
        reduce { state.copy(settings = newSettings) }
        updateSettings(newSettings)
    }

    fun changeLanguageButtonClicked() = intent {
        postSideEffect(SettingsSideEffects.ChangeLanguage)
    }

    fun notificationSettingsButtonClicked() = intent {
        postSideEffect(SettingsSideEffects.OpenNotificationSettings)
    }

    fun backClicked() = intent {
        postSideEffect(SettingsSideEffects.Back)
    }
}