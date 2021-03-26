package at.sunilson.doistillneedthisthing.shared.domain.entities

import android.net.Uri

data class Item(
    val id: Int,
    val name: String,
    val image: Uri,
    val addedTimestamp: Long,
    val lastCheckedTimestamp: Long = NEVER_CHECKED,
    val stillNeeded: Boolean = true,
    val notNeededAnymoreTimestamp: Long? = null,
    val location: String? = null
) {

    companion object {
        const val NEVER_CHECKED = -1L
    }

}
