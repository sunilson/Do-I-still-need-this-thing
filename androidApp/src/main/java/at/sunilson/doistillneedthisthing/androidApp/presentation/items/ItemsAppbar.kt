package at.sunilson.doistillneedthisthing.androidApp.presentation.items

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.composables.rememberScrollElevation
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun ItemsAppBar(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    onMenuClick: () -> Unit
) {
    val elevation by rememberScrollElevation(scrollState)
    TopAppBar(
        title = {},
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier
            .zIndex(1f)
            .statusBarsPadding(),
        elevation = elevation.dp,
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = stringResource(R.string.menu_button_content_description)
                )
            }
        })
}