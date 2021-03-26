package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.presentation.additem.AddItemSideEffects.AskForPermissions
import at.sunilson.doistillneedthisthing.androidApp.presentation.additem.AddItemSideEffects.ShowRationale
import at.sunilson.ktx.context.nightMode
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect

class AddItemFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var settingsResult: ActivityResultLauncher<Intent>
    private val viewModel by viewModel<AddItemViewModel>()
    private var camera: Camera? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.cameraAccessWrapper.applySystemWindowInsetsToPadding(top = true)
        //binding.permissionsError.applySystemWindowInsetsToPadding(top = true)
        viewModel.viewCreated()
        observeState()
        observeSideEffecs()
        setupBackListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityResultListener()
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(requireContext().nightMode)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.stateFlow.collect { state ->
                when (state) {
                    AddItemState.Initial -> {
                        // Nothing here
                    }
                    AddItemState.Camera -> startCamera()
                    AddItemState.PermissionMissing -> {
                        // TODO Show permission missing screen
                    }
                }
            }
        }
    }

    private fun setupBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.backClicked()
                }
            }
        )
    }

    private fun observeSideEffecs() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    AskForPermissions -> checkCameraPermission()
                    ShowRationale -> MaterialAlertDialogBuilder(requireContext())
                        .setTitle("TODO")
                        .setMessage("TODO")
                        .setPositiveButton("TODO") { _, _ -> viewModel.rationaleAccepted() }
                        .setNegativeButton("TODO") { _, _ -> viewModel.rationaleDenied() }
                        .show()
                    AddItemSideEffects.Back -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setupActivityResultListener() {
        activityResultLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.permissionsGranted()
            } else {
                viewModel.permissionsDenied(shouldShowRequestPermissionRationale(CAMERA))
            }
        }

        settingsResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                activityResultLauncher.launch(CAMERA)
            }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PERMISSION_GRANTED) {
            viewModel.permissionsGranted()
        } else {
            activityResultLauncher.launch(CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            try {
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    createImagePreview()
                )
            } catch (error: IllegalArgumentException) {
                // TODO No Camera found
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun createImagePreview(): Preview {
        return Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(view?.findViewById<PreviewView>(R.id.preview_view)?.surfaceProvider) }
    }
}