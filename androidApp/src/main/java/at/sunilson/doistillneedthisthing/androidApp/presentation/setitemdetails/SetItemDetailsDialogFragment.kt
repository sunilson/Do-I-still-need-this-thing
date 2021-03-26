package at.sunilson.doistillneedthisthing.androidApp.presentation.setitemdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.databinding.FragmentComposeBinding
import at.sunilson.ktx.fragment.delegates.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.composethemeadapter.MdcTheme

class SetItemDetailsDialogFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentComposeBinding::bind)

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
                Surface(color = MaterialTheme.colors.background) {
                    SetItemDetails()
                }
            }
        }
    }
}