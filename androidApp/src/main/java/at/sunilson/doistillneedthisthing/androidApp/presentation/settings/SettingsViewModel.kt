package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.shared.domain.GetSettings
import at.sunilson.doistillneedthisthing.shared.domain.UpdateSettings
import at.sunilson.doistillneedthisthing.shared.domain.entities.Settings
import com.github.michaelbull.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

sealed class SettingsSideEffects {
    object OpenNotificationSettings : SettingsSideEffects()
    object ChangeLanguage : SettingsSideEffects()
    object Back : SettingsSideEffects()
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
            Timber.d("Loaded settings $settings")
        }
    }

    fun notificationsEnabledToggled(checked: Boolean) = intent {
        val newSettings = state.settings?.copy(notificationsEnabled = checked) ?: return@intent
        reduce { state.copy(settings = newSettings) }
        updateSettings(newSettings).fold(
            { Timber.d("Updated notifications enabled to: $checked") },
            { Timber.e(it, "Could not update settings!") }
        )
    }

    fun changeLanguageButtonClicked() = intent {
        postSideEffect(SettingsSideEffects.ChangeLanguage)
    }

    fun notificationSettingsButtonClicked() = intent {
        postSideEffect(SettingsSideEffects.OpenNotificationSettings)
    }

    fun randomSingleDecisionsPerDayChanged(newValue: Float) = intent {
        val newSettings =
            state.settings?.copy(randomSingleDecisionsPerDay = newValue.toInt()) ?: return@intent
        reduce { state.copy(settings = newSettings) }
        updateSettings(newSettings).fold(
            { Timber.d("Updated random single decisions per day to ${newValue.toInt()}") },
            { Timber.e(it, "Could not update settings!") }
        )
    }

    fun weeklyDecisionsChanged(newValue: Float) = intent {
        val newSettings =
            state.settings?.copy(decisionsPerWeekly = newValue.toInt()) ?: return@intent
        reduce { state.copy(settings = newSettings) }
        updateSettings(newSettings).fold(
            { Timber.d("Updated weekly decisions to ${newValue.toInt()}") },
            { Timber.e(it, "Could not update settings!") }
        )
    }

    fun backClicked() = intent {
        postSideEffect(SettingsSideEffects.Back)
    }
}