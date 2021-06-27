package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.databinding.FragmentAddItemBinding
import at.sunilson.doistillneedthisthing.androidApp.presentation.setitemdetails.SetItemDetailsDialogFragment.Companion.IMAGE_URI
import at.sunilson.ktx.context.hasPermission
import at.sunilson.ktx.fragment.delegates.viewBinding
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.ktx.navigation.navigateSafe
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.resume

@AndroidEntryPoint
class AddItemFragment : Fragment(R.layout.fragment_add_item) {

    private val binding by viewBinding(FragmentAddItemBinding::bind)
    private val viewModel by viewModels<AddItemViewModel>()
    private var currentRotation: Int = 0
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var orientationEventListener: OrientationEventListener? = null
    private val objecteDetectionAnalyzer: ObjectDetectionAnalyzer by lazy {
        ObjectDetectionAnalyzer { objectDetectionResult ->
            view
                ?.findViewById<ObjectBoundingBoxesView>(R.id.object_bounding_boxes_view)
                ?.objectDetectionResult = objectDetectionResult
        }.apply { active = false }
    }

    @ExperimentalPermissionsApi
    @ExperimentalAnimationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            MdcTheme {
                AddItemOverlay()
                AddItemPermissions()
            }
        }
        viewModel.viewCreated()
        observeState()
        observeSideEffecs()
        observeSelectedRect()
        setupBackListener()
        setupRotationListener()
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener?.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener?.disable()
    }

    override fun onResume() {
        super.onResume()
        drawBelowStatusBar()
        setStatusBarColor(android.R.color.transparent)
        useLightStatusBarIcons(true)
    }

    private fun setupRotationListener() {
        orientationEventListener?.disable()
        orientationEventListener = object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                currentRotation = orientation
                val rotation: Int = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                Timber.d("Current rotation is $currentRotation")
                imageCapture?.targetRotation = rotation
            }
        }
        orientationEventListener?.enable()
    }

    private suspend fun takeImage() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val imageFile =
                File(
                    requireContext().getExternalFilesDir(null),
                    "${System.currentTimeMillis()}.jpg"
                )
            val fileOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
            val uri = suspendCancellableCoroutine<Uri?> { cont ->
                imageCapture?.takePicture(
                    fileOptions,
                    Executors.newSingleThreadExecutor(),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            cont.resume(Uri.fromFile(imageFile))
                        }

                        override fun onError(exception: ImageCaptureException) {
                            cont.resume(null)
                        }
                    })
            }
            viewModel.imageCaptured(uri ?: return@launch)
        }
    }

    private fun observeSelectedRect() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.objectBoundingBoxesView.selectedRect.collect { rectF ->
                val bitmap = binding.previewView.bitmap
                viewModel.detectedObjectSelected(bitmap, currentRotation, rectF)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.stateFlow.collect { state ->
                when (state) {
                    AddItemState.Initial -> {
                        // Nothing here
                    }
                    is AddItemState.Camera -> {
                        startCamera()
                        camera?.cameraControl?.enableTorch(state.torchEnabled)
                        objecteDetectionAnalyzer.active = state.objectDetectionEnabled
                        if (!state.objectDetectionEnabled) {
                            binding.objectBoundingBoxesView.objectDetectionResult = null
                        }
                        binding.objectBoundingBoxesView.isVisible = !state.processingImage
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
                    AddItemSideEffects.Back -> findNavController().navigateUp()
                    is AddItemSideEffects.ImageTaken -> {
                        navigateSafe(
                            R.id.set_item_details,
                            bundleOf(IMAGE_URI to it.uri)
                        )
                    }
                    AddItemSideEffects.TakeImage -> takeImage()
                }
            }
        }
    }

    private fun startCamera() {
        if (camera != null) return
        if (!requireContext().hasPermission(Manifest.permission.CAMERA)) return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            try {
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    createImagePreview(),
                    setupImageAnalysis(),
                    setupImageCapture()
                )
            } catch (error: IllegalArgumentException) {
                // No camera found 🤷‍
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupImageCapture(): ImageCapture {
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(requireView().display.rotation)
            .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
        return imageCapture!!
    }

    private fun setupImageAnalysis(): ImageAnalysis {
        return ImageAnalysis
            .Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(Executors.newSingleThreadExecutor(), objecteDetectionAnalyzer) }
    }

    private fun createImagePreview(): Preview {
        return Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(view?.findViewById<PreviewView>(R.id.preview_view)?.surfaceProvider) }
    }
}