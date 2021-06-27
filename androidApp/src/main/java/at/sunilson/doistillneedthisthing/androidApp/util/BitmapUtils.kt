package at.sunilson.doistillneedthisthing.androidApp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BitmapUtils {
    suspend fun loadBitmapFromUri(
        context: Context,
        uri: Uri,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Bitmap = withContext(dispatcher) {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}