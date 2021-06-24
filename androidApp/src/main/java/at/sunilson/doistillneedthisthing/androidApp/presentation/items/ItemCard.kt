package at.sunilson.doistillneedthisthing.androidApp.presentation.items

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.theme.Spacings
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item.State.NEEDED
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item.State.REMOVED
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter

@Composable
@Preview
private fun ItemCardPreview() {
    ItemCard(
        item = Item(
            "Name",
            "https://s.w-x.co/de-igel-winter-GettyImages-1179776272.jpg",
            addedTimestamp = System.currentTimeMillis()
        ),
        onItemNeeded = {},
        onItemNotNeeded = {},
        onItemRemoved = {},
        onItemDelete = {}
    )
}

@Composable
fun ItemCard(
    item: Item,
    onItemNeeded: () -> Unit,
    onItemNotNeeded: () -> Unit,
    onItemRemoved: () -> Unit,
    onItemDelete: () -> Unit
) {
    var showNotNeededDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }

    if (showDetailsDialog) {
        ItemDetailsDialog(item = item) { showDetailsDialog = false }
    }

    if (showNotNeededDialog) {
        AlertDialog(
            onDismissRequest = { showNotNeededDialog = false },
            title = { Text("Mark for removal") },
            text = { Text("Are you sure you don't need this anymore?") },
            confirmButton = {
                TextButton(onClick = {
                    showNotNeededDialog = false
                    onItemNotNeeded()
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showNotNeededDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Removed") },
            text = { Text("Did get rid of this item?") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveDialog = false
                    onItemRemoved()
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        elevation = 3.dp,
        modifier = Modifier
            .padding(
                top = Spacings.contentPadding,
                start = Spacings.contentPadding,
                end = Spacings.contentPadding
            )
            .fillMaxWidth()
            .clickable { showDetailsDialog = true }
    ) {
        ConstraintLayout(Modifier.padding(Spacings.contentPadding)) {
            val (thumbnail, title, subTitle, notNeededButton, trashButton) = createRefs()
            createVerticalChain(title, subTitle, chainStyle = ChainStyle.Packed)

            Image(
                painter = rememberCoilPainter(
                    Uri.parse(item.imagePath),
                    requestBuilder = { transformations(CircleCropTransformation()) },
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .constrainAs(thumbnail) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                item.name,
                style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onSurface),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(title) {
                        width = Dimension.fillToConstraints
                        start.linkTo(thumbnail.end, margin = 16.dp)
                        end.linkTo(notNeededButton.start, margin = 16.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(subTitle.top)

                    })

            Text(
                item.location ?: "No location",
                style = MaterialTheme.typography.subtitle1,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(subTitle) {
                        width = Dimension.fillToConstraints
                        start.linkTo(thumbnail.end, margin = 16.dp)
                        end.linkTo(notNeededButton.start, margin = 16.dp)
                        top.linkTo(title.bottom)
                        bottom.linkTo(parent.bottom)
                    })

            // Dont show action buttons on removed item
            if (item.state == REMOVED) return@ConstraintLayout

            IconButton(
                onClick = {
                    if (item.state == NEEDED) {
                        showNotNeededDialog = true
                    } else {
                        showRemoveDialog = true
                    }
                },
                modifier = Modifier.constrainAs(notNeededButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                Icon(
                    if (item.state == NEEDED) Icons.Rounded.ThumbDown else Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}