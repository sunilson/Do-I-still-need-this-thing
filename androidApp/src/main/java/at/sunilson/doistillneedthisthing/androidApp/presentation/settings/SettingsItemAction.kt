package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.theme.Spacings

@Composable
fun SettingsItemAction(
    title: String,
    subTitle: String,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacings.contentPadding)
            ) {
                val (titleText, subTitleText, switch) = createRefs()
                createVerticalChain(titleText, subTitleText, chainStyle = ChainStyle.Packed)
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.constrainAs(titleText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(subTitleText.top)
                    })
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(subTitleText) {
                        start.linkTo(parent.start)
                        top.linkTo(titleText.bottom, 8.dp)
                        bottom.linkTo(parent.bottom)
                    })
                Icon(
                    Icons.Rounded.ChevronRight,
                    modifier = Modifier.constrainAs(switch) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    },
                    contentDescription = ""
                )
            }
            if (showDivider) Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsActionItemPreview() {
    SettingsItemAction(title = "Title", subTitle = "SubTitle")
}