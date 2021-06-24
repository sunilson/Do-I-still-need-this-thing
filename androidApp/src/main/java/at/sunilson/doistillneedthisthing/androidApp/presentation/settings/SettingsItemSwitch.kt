package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.theme.Spacings

@Composable
fun SettingsItemSwitch(
    title: String,
    subTitle: String,
    enabled: Boolean,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onChange: (Boolean) -> Unit = {},
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Column(modifier = Modifier.clickable { onChange(!enabled) }) {
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
                        end.linkTo(switch.start, 8.dp)
                        bottom.linkTo(subTitleText.top)
                        width = Dimension.fillToConstraints
                    })
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(subTitleText) {
                        start.linkTo(parent.start)
                        end.linkTo(switch.start, 8.dp)
                        top.linkTo(titleText.bottom, 8.dp)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    })
                Switch(
                    checked = enabled,
                    onCheckedChange = onChange,
                    modifier = Modifier.constrainAs(switch) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    }
                )
            }
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsSwitchItemPreview() {
    SettingsItemSwitch(title = "Title", subTitle = "SubTitle", enabled = true)
}