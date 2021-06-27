package at.sunilson.doistillneedthisthing.androidApp.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.theme.Spacings
import kotlin.math.roundToInt

@Composable
fun SettingsItemSlider(
    title: String,
    subTitle: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    steps: Int = 100,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    showDivider: Boolean = true,
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Column {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacings.contentPadding)
            ) {
                val (titleText, subTitleText, slider, counter) = createRefs()
                createVerticalChain(titleText, subTitleText, slider, chainStyle = ChainStyle.Packed)
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.constrainAs(titleText) {
                        width = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(counter.start, 8.dp)
                        bottom.linkTo(subTitleText.top)
                    })
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(subTitleText) {
                        width = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        end.linkTo(counter.start, 8.dp)
                        top.linkTo(titleText.bottom, 8.dp)
                        bottom.linkTo(slider.top)
                    })
                Text(
                    text = value.roundToInt().toString(),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(counter) {
                        end.linkTo(parent.end)
                        top.linkTo(titleText.top)
                        bottom.linkTo(subTitleText.bottom)
                    })
                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    onValueChangeFinished = onValueChangeFinished,
                    steps = steps,
                    valueRange = valueRange,
                    modifier = Modifier.constrainAs(slider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(subTitleText.bottom)
                    }
                )
            }
            if (showDivider) Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsItemSliderPreview() {
    SettingsItemSlider(
        title = "Title",
        subTitle = "SubTitle",
        value = 20f,
        onValueChange = {},
        onValueChangeFinished = {},
        steps = 20,
        valueRange = 0f..20f
    )
}