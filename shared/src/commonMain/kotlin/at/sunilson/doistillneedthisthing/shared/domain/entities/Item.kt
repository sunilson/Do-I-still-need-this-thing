package at.sunilson.doistillneedthisthing.shared.domain.entities

data class Item(
    val name: String,
    val imagePath: String,
    val addedTimestamp: Long,
    val lastCheckedTimestamp: Long? = null,
    val markedForRemovalTimestamp: Long? = null,
    val removedTimestamp: Long? = null,
    val location: String? = null
) {
    var id: Long = NO_ID

    val state: State
        get() = when {
            removedTimestamp != null -> State.REMOVED
            markedForRemovalTimestamp != null -> State.NOT_NEEDED
            else -> State.NEEDED
        }

    companion object {
        const val NO_ID = -1L
    }

    enum class State {
        NEEDED, NOT_NEEDED, REMOVED
    }
}
