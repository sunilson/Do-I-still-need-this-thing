package at.sunilson.doistillneedthisthing.shared.data

import at.sunilson.doistillneedthisthing.DatabaseItem
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item

fun DatabaseItem.toItem() = Item(
    name,
    image_path,
    added_timestamp,
    last_checked_timestamp,
    marked_for_removal_timestamp,
    removal_timestamp,
    location
).apply {
    this.id = this@toItem.id
}

fun Item.toDatabaseItem() = DatabaseItem(
    id,
    name,
    imagePath,
    addedTimestamp,
    lastCheckedTimestamp,
    markedForRemovalTimestamp,
    removedTimestamp,
    location
)