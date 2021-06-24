package at.sunilson.doistillneedthisthing.shared.domain.extensions

import android.net.Uri
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item

val Item.imageUri
    get() = Uri.parse(imagePath)