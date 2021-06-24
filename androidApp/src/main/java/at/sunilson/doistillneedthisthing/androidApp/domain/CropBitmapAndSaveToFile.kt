package at.sunilson.doistillneedthisthing.androidApp.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import at.sunilson.doistillneedthisthing.androidApp.domain.entities.CropBitmapParameters
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

// TODO Do this in multiplatform module
class CropBitmapAndSaveToFile @Inject constructor(@ApplicationContext private val application: Context) :
    UseCase<CropBitmapParameters, Uri>() {
    override suspend fun run(params: CropBitmapParameters): Uri {
        val (bitmap, rotation, rect) = params

        val adjustedRotation = when (rotation) {
            in 45..134 -> 90f
            in 135..224 -> 180f
            in 225..314 -> 270f
            else -> 0f
        }

        val x = maxOf(rect.left - 100, 0)
        val y = maxOf(rect.top - 100, 0)
        val width = minOf(rect.right - rect.left + 200, bitmap.width - x)
        val height = minOf(rect.bottom - rect.top + 200, bitmap.height - y)
        val rotationMatrix = Matrix().apply { postRotate(adjustedRotation) }
        val resultBitmap =
            Bitmap.createBitmap(params.bitmap, x, y, width, height, rotationMatrix, true)

        val imageFile =
            File(application.getExternalFilesDir(null), "${System.currentTimeMillis()}.jpg")
        FileOutputStream(imageFile).use {
            resultBitmap.compress(
                Bitmap.CompressFormat.WEBP,
                100,
                it
            )
        }
        return Uri.fromFile(imageFile)
    }
}