package at.sunilson.doistillneedthisthing.androidApp.presentation.shared.base

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    protected val bottomSheetBehavior: BottomSheetBehavior<FrameLayout>?
        get() = (dialog as? BottomSheetDialog)?.behavior

    @SuppressLint("RestrictedApi", "VisibleForTests")
    @Suppress("MagicNumber")
    override fun onStart() {
        super.onStart()
        val bottomSheetView = dialog
            ?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheetView?.doOnLayout {
            // Disable shape animations so rounded corners stay in expanded mode
            bottomSheetBehavior?.disableShapeAnimations()
        }
    }
}
