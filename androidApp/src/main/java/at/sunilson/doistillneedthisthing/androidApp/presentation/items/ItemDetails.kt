package at.sunilson.doistillneedthisthing.androidApp.presentation.items

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.theme.Spacings
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun ItemDetailsDialog(
    item: Item,
    onItemNeeded: () -> Unit = {},
    onItemNotNeeded: () -> Unit = {},
    onItemRemoved: () -> Unit = {},
    onItemDelete: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        ItemDetails(
            item,
            onItemNeeded,
            onItemNotNeeded,
            onItemRemoved,
            onItemDelete
        )
    }
}

@Composable
fun ItemDetails(
    item: Item,
    onItemNeeded: () -> Unit = {},
    onItemNotNeeded: () -> Unit = {},
    onItemRemoved: () -> Unit = {},
    onItemDelete: () -> Unit = {}
) {
    Surface(shape = MaterialTheme.shapes.medium) {
        ConstraintLayout {
            val (image, name, addedTime, location, buttonPrimary, buttonSecondary) = createRefs()
            Image(
                painter = rememberCoilPainter(Uri.parse(item.imagePath), fadeIn = true),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colors.secondary)
                    .constrainAs(image) { top.linkTo(parent.top) }
            )

            if (item.location != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(Spacings.contentPadding)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Black.copy(0.4f))
                        .padding(8.dp)
                        .constrainAs(location) {
                            bottom.linkTo(image.bottom)
                            end.linkTo(parent.end)
                        }
                ) {
                    Icon(
                        Icons.Rounded.LocationOn,
                        contentDescription = "TODO",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = item.location.orEmpty(),
                        color = Color.White,
                        style = MaterialTheme.typography.caption
                    )
                }
            }

            Text(
                text = item.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.constrainAs(name) {
                    top.linkTo(image.bottom, Spacings.contentPadding)
                    start.linkTo(parent.start, Spacings.contentPadding)
                }
            )

            Text(
                text = "Hinzugefügt am 20.12.1993 um 20:01",
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(ContentAlpha.medium),
                modifier = Modifier.constrainAs(addedTime) {
                    top.linkTo(name.bottom, 4.dp)
                    start.linkTo(parent.start, Spacings.contentPadding)
                }
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(buttonPrimary) {
                    start.linkTo(parent.start, Spacings.contentPadding)
                    end.linkTo(parent.end, Spacings.contentPadding)
                    top.linkTo(addedTime.bottom, 32.dp)
                    width = Dimension.fillToConstraints
                }
            ) {
                Text(
                    text = when (item.state) {
                        Item.State.NEEDED -> "I don't need this"
                        Item.State.NOT_NEEDED -> "I removed this"
                        Item.State.REMOVED -> "I still need this"
                    }
                )
            }

            OutlinedButton(
                onClick = { /* Not implemented */ },
                modifier = Modifier.constrainAs(buttonSecondary) {
                    start.linkTo(parent.start, Spacings.contentPadding)
                    end.linkTo(parent.end, Spacings.contentPadding)
                    top.linkTo(buttonPrimary.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, Spacings.contentPadding)
                    width = Dimension.fillToConstraints
                }
            ) {
                Text(
                    text = when (item.state) {
                        Item.State.NEEDED -> "I removed this"
                        Item.State.NOT_NEEDED -> "I still need this"
                        Item.State.REMOVED -> "Delete from archive"
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemDetailsPreview() {
    ItemDetails(
        item = Item(
            "Name",
            "https://s.w-x.co/de-igel-winter-GettyImages-1179776272.jpg",
            location = "Wohnzimmer",
            addedTimestamp = System.currentTimeMillis()
        )
    )
}