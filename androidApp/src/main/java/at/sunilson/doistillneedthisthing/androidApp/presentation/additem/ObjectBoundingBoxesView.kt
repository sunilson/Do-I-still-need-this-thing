package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRect
import androidx.core.graphics.toRectF
import com.google.android.material.math.MathUtils
import com.google.mlkit.vision.objects.DetectedObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

private data class DrawObject(val rect: RectF, val alpha: Float = 1f)

class ObjectBoundingBoxesView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    var objectDetectionResult: ObjectDetectionResult? = null
        set(value) {
            if (value == null) {
                interpolator?.cancel()
                interpolate(field?.copy(), null)
            } else if (interpolator?.isRunning != true) {
                val newObjects = scaleAndOffsetBoxes(value)
                interpolate(field?.copy(), newObjects)
                field = newObjects
            }
        }

    private var interpolatedDrawObjects: List<DrawObject> = listOf()
        set(value) {
            field = value
            invalidate()
        }

    private val _selectedRect = MutableStateFlow<RectF?>(null)
    val selectedRect: Flow<RectF>
        get() = _selectedRect.filterNotNull()

    private var interpolator: ValueAnimator? = null

    private val rectPaint = Paint().apply {
        strokeWidth = 5f
        color = Color.WHITE
        style = Paint.Style.STROKE
    }

    /**
     * Intercept touch events to check if a rectangle has been touched. We always choose the smallest
     * one inside the touch area
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        _selectedRect.tryEmit(
            interpolatedDrawObjects
                .map { it.rect }
                .filter { rect -> rect.contains(event.x, event.y) }
                .minByOrNull { rect -> (rect.width() * rect.height()) }
        )
        return super.onTouchEvent(event)
    }

    /**
     * Animate between detection results for a smoother user experience
     */
    private fun interpolate(old: ObjectDetectionResult?, new: ObjectDetectionResult?) {
        interpolator = ValueAnimator
            .ofFloat(0f, 1f)
            .setDuration(INTERPOLATION_DURATION)
            .apply {
                addUpdateListener {
                    val progress = it.animatedValue as Float

                    val oldObjects = old?.objects?.toMutableList() ?: mutableListOf()
                    val newObjects = new?.objects?.toMutableList() ?: mutableListOf()
                    val result = mutableListOf<DrawObject>()

                    oldObjects.forEach { obj ->
                        // Fade out if id does not exist in new objects
                        if (!newObjects.any { it.trackingId == obj.trackingId }) {
                            result.add(DrawObject(obj.boundingBox.toRectF(), 1f - progress))
                        }
                    }

                    newObjects.forEach { obj ->
                        val previous = oldObjects.firstOrNull { it.trackingId == obj.trackingId }
                        if (previous == null) {
                            result.add(DrawObject(obj.boundingBox.toRectF()))
                            return@forEach
                        }

                        val differenceX = previous.boundingBox.left - obj.boundingBox.left
                        val differenceY = previous.boundingBox.top - obj.boundingBox.top
                        result.add(
                            DrawObject(
                                RectF(
                                    previous.boundingBox.left - differenceX * progress,
                                    previous.boundingBox.top - differenceY * progress,
                                    previous.boundingBox.right - differenceX * progress,
                                    previous.boundingBox.bottom - differenceY * progress
                                )
                            )
                        )
                    }

                    interpolatedDrawObjects = result
                }
                start()
            }
    }

    /**
     * Results from CameraX are from a smaller version of the image and need to be scaled up to
     * fit the objects seen in the camera viewfinder.
     */
    private fun scaleAndOffsetBoxes(result: ObjectDetectionResult?): ObjectDetectionResult? {
        if (result == null) return null
        val scaleFactor = maxOf(width / result.imageHeight, height / result.imageWidth).toFloat()
        return result.copy(objects = result.objects.map { detectionResult ->
            val offsetX = (width - result.imageHeight * scaleFactor) / 2
            val offsetY = (height - result.imageWidth * scaleFactor) / 2
            val newRect = RectF().apply {
                left = detectionResult.boundingBox.left * scaleFactor + offsetX
                right = detectionResult.boundingBox.right * scaleFactor + offsetX
                top = detectionResult.boundingBox.top * scaleFactor + offsetY
                bottom = detectionResult.boundingBox.bottom * scaleFactor + offsetY
            }.toRect()
            DetectedObject(newRect, detectionResult.trackingId, detectionResult.labels)
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        interpolatedDrawObjects.forEach {
            rectPaint.alpha = MathUtils.lerp(0f, 255f, it.alpha).toInt()
            canvas.drawRoundRect(
                it.rect,
                30f,
                30f,
                rectPaint
            )
        }
    }

    companion object {
        const val INTERPOLATION_DURATION = 200L
    }
}