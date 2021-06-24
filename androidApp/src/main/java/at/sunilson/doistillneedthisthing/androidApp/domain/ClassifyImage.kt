package at.sunilson.doistillneedthisthing.androidApp.domain

import android.graphics.Bitmap
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ClassifyImage @Inject constructor(private val imageLabeler: ImageLabeler) :
    UseCase<Bitmap, String?>() {
    override suspend fun run(params: Bitmap): String? {
        val image = InputImage.fromBitmap(params, 0)
        val results = suspendCancellableCoroutine<List<ImageLabel>> { cont ->
            imageLabeler
                .process(image)
                .addOnSuccessListener { cont.resume(it) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
        val highestConfidenceResult = results.maxByOrNull { it.confidence }
        return highestConfidenceResult?.text
    }
}