package at.sunilson.doistillneedthisthing.androidApp.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL
import android.media.ExifInterface.ORIENTATION_FLIP_VERTICAL
import android.media.ExifInterface.ORIENTATION_ROTATE_180
import android.media.ExifInterface.ORIENTATION_ROTATE_270
import android.media.ExifInterface.ORIENTATION_ROTATE_90
import android.media.ExifInterface.ORIENTATION_TRANSPOSE
import android.media.ExifInterface.ORIENTATION_TRANSVERSE
import android.net.Uri
import androidx.core.net.toFile
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import javax.inject.Inject


class RotateBitmapFromExifData @Inject constructor() : UseCase<Uri, Bitmap>() {
    override suspend fun run(params: Uri): Bitmap {
        val inputFile = params.toFile()
        val bitmap = BitmapFactory.decodeFile(inputFile.path)
        val orientation = ExifInterface(inputFile.path).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val matrix = Matrix()
        when (orientation) {
            ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
        }

        val resultBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        //bitmap.recycle()
        return resultBitmap
    }
}