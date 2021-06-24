package at.sunilson.doistillneedthisthing.androidApp.domain.entities

import android.graphics.Bitmap
import android.graphics.Rect

data class CropBitmapParameters(val bitmap: Bitmap, val rotation: Int, val rect: Rect)