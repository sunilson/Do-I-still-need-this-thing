package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.databinding.FragmentComposeBinding
import at.sunilson.ktx.fragment.delegates.viewBinding
import com.google.android.material.composethemeadapter.MdcTheme
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(R.layout.fragment_compose) {

    private val viewModel by viewModel<HomeViewModel>()
    private val binding by viewBinding(FragmentComposeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            MdcTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Home(viewModel)
                }
            }
        }
        observeSideEffects()
    }

    private fun observeSideEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    HomeSideEffects.Back -> findNavController().navigateUp()
                    HomeSideEffects.AddItem -> findNavController().navigate(R.id.add_item)
                }
            }
        }
    }

}