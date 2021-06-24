package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.databinding.FragmentComposeBinding
import at.sunilson.ktx.fragment.delegates.viewBinding
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setStatusBarThemeColor
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_compose) {

    private val binding by viewBinding(FragmentComposeBinding::bind)
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            // Workaround to get viewModel() working with constructor arguments in composables
            CompositionLocalProvider(LocalViewModelStoreOwner provides this) {
                MdcTheme {
                    ProvideWindowInsets {
                        Surface(color = MaterialTheme.colors.background) {
                            Settings()
                        }
                    }
                }
            }
        }

        observeSideEffects()
    }

    override fun onResume() {
        super.onResume()
        drawBelowStatusBar()
        drawBelowStatusBar()
        setStatusBarThemeColor(R.attr.colorSurface)
        useLightStatusBarIcons(false)
    }

    private fun observeSideEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.sideEffectFlow.collect { effect ->
                when (effect) {
                    SettingsSideEffects.OpenNotificationSettings -> openNotificationSettings()
                    SettingsSideEffects.ChangeLanguage -> openLanguageSettings()
                    SettingsSideEffects.Back -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun openLanguageSettings() {
        startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun openNotificationSettings() {
        startActivity(Intent().apply {
            action = "android.settings.APP_NOTIFICATION_SETTINGS"
            putExtra(
                "android.provider.extra.APP_PACKAGE",
                requireContext().packageName
            );
            putExtra("app_package", requireContext().packageName);
            putExtra("app_uid", requireContext().applicationInfo.uid);
        })
    }
}