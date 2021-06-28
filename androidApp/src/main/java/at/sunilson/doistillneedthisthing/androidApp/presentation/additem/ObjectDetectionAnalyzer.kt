package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

data class ObjectDetectionResult(
    val imageWidth: Int,
    val imageHeight: Int,
    val objects: List<DetectedObject>
)

class ObjectDetectionAnalyzer(private val cb: (ObjectDetectionResult) -> Unit) :
    ImageAnalysis.Analyzer {

    var active: Boolean = true

    private val objectDetector: ObjectDetector by lazy {
        val options =
            ObjectDetectorOptions
                .Builder()
                .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
        ObjectDetection.getClient(options)
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null || !active) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        objectDetector
            .process(image)
            .addOnSuccessListener { result ->
                cb(
                    ObjectDetectionResult(
                        mediaImage.width,
                        mediaImage.height,
                        result
                    )
                )
            }
            .addOnFailureListener {}
            .addOnCompleteListener { imageProxy.close() }
    }
}

