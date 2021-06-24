package at.sunilson.doistillneedthisthing.androidApp.presentation.setitemdetails

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.databinding.FragmentComposeBinding
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.base.BaseBottomSheetDialogFragment
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.delegates.viewBinding
import at.sunilson.ktx.fragment.setStatusBarColor
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SetItemDetailsDialogFragment : BaseBottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentComposeBinding::bind)
    private val viewModel by viewModels<SetItemDetailsViewModel>()
    private val imageUri: Uri
        get() = requireArguments().getParcelable(IMAGE_URI) ?: error("No image provided!")

    override fun onResume() {
        super.onResume()
        setStatusBarColor(android.R.color.transparent, dialog?.window)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            MdcTheme {
                ProvideWindowInsets {
                    Surface(color = Color.Transparent) {
                        SetItemDetails(imageUri)
                    }
                }
            }
        }

        observeSideEffects()
    }

    private fun observeSideEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    SetItemDetailsSideEffects.ItemAdded -> {
                        requireContext().showToast("Item added!")
                        findNavController().navigateUp()
                    }
                    is SetItemDetailsSideEffects.ItemAddingFailed -> {
                        requireContext().showToast("Item adding failed!")
                    }
                }
            }
        }
    }

    companion object {
        const val IMAGE_URI = "imageUri"
    }
}