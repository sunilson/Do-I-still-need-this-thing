package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_compose) {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding(FragmentComposeBinding::bind)

    @ExperimentalAnimationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Insetter.builder().padding(windowInsetTypesOf(statusBars = true)).applyToView(view)
        binding.composeView.setContent {
            // Workaround to get viewModel() working with constructor arguments in composables
            CompositionLocalProvider(LocalViewModelStoreOwner provides this) {
                MdcTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        Home()
                    }
                }
            }
        }
        observeSideEffects()
    }

    override fun onResume() {
        super.onResume()
        drawBelowStatusBar()
        setStatusBarThemeColor(R.attr.colorSurface)
        useLightStatusBarIcons(false)
    }

    private fun observeSideEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    HomeSideEffects.Back -> findNavController().navigateUp()
                    HomeSideEffects.AddItem -> findNavController().navigate(R.id.add_item)
                    HomeSideEffects.OpenSettings -> findNavController().navigate(R.id.show_settings)
                }
            }
        }
    }
}