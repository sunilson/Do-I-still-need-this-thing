package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.composables.rememberScrollElevation
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Settings(viewModel: SettingsViewModel = viewModel()) {
    val state = viewModel.container.stateFlow.collectAsState(initial = SettingsState())
    val settings = state.value.settings ?: return
    val scrollState = rememberScrollState()
    val elevation by rememberScrollElevation(scrollState)
    var dailySliderState by remember { mutableStateOf(settings.randomSingleDecisionsPerDay.toFloat()) }
    var weeklySliderState by remember { mutableStateOf(settings.decisionsPerWeekly.toFloat()) }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (menuBar, itemList) = createRefs()
        TopAppBar(
            title = {},
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .zIndex(1f)
                .statusBarsPadding()
                .constrainAs(menuBar) { top.linkTo(parent.top) },
            elevation = elevation.dp,
            navigationIcon = {
                IconButton(onClick = { viewModel.backClicked() }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.menu_button_content_description)
                    )
                }
            })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .constrainAs(itemList) {
                    height = Dimension.fillToConstraints
                    top.linkTo(menuBar.bottom)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            SettingsItemSwitch(
                title = stringResource(R.string.settings_all_notifications_title),
                subTitle = stringResource(R.string.settings_all_notifications_subtitle),
                enabled = settings.notificationsEnabled
            ) { viewModel.notificationsEnabledToggled(it) }
            SettingsItemAction(
                title = stringResource(R.string.settings_notifications_title),
                subTitle = stringResource(R.string.settings_notifications_subtitle)
            ) { viewModel.notificationSettingsButtonClicked() }
            SettingsItemSlider(
                title = stringResource(R.string.settings_random_decisions_amount_title),
                subTitle = stringResource(R.string.settings_random_decisions_amount_description),
                value = dailySliderState,
                valueRange = 0f..10f,
                onValueChange = { dailySliderState = it },
                onValueChangeFinished = {
                    viewModel.randomSingleDecisionsPerDayChanged(
                        dailySliderState
                    )
                },
                steps = 10
            )
            SettingsItemSlider(
                title = stringResource(R.string.settings_weekly_amount_title),
                subTitle = stringResource(R.string.settings_weekly_amount_description),
                value = weeklySliderState,
                valueRange = 0f..20f,
                onValueChange = { weeklySliderState = it },
                onValueChangeFinished = { viewModel.weeklyDecisionsChanged(dailySliderState) },
                steps = 20
            )
            SettingsItemAction(
                title = stringResource(R.string.settings_language_title),
                subTitle = stringResource(R.string.settings_language_subtitle),
                showDivider = false
            ) { viewModel.changeLanguageButtonClicked() }
        }
    }
}