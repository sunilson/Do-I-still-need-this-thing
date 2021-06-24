package at.sunilson.doistillneedthisthing.androidApp.presentation.setitemdetails

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.extensions.orElse
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.imePadding

@Composable
fun SetItemDetails(imageUri: Uri, viewModel: SetItemDetailsViewModel = viewModel()) {
    LaunchedEffect(true) { viewModel.imageReceived(imageUri) }

    val state = viewModel.container.stateFlow.collectAsState(initial = SetItemDetailsState())
    val image = state.value.processedBitmap
    val classifiedText = state.value.classifiedLabel
    val insets = LocalWindowInsets.current

    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .background(Color(0xFFEDEDED), shape = RoundedCornerShape(2.dp))
                .width(60.dp)
                .height(4.dp)
        )

        Crossfade(targetState = image) { image ->
            if (image != null) {
                Image(
                    image.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.height(150.dp)
                )
            } else {
                Box(modifier = Modifier.height(150.dp))
            }
        }

        OutlinedTextField(
            value = title.orElse { classifiedText.orEmpty() },
            label = { Text("Name") },
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            onValueChange = { title = it }
        )
        OutlinedTextField(
            value = location.orEmpty(),
            label = { Text("Location") },
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            onValueChange = { location = it }
        )

        Button(
            onClick = { viewModel.addItemClicked(title, imageUri, location) },
            enabled = title.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.primary_button_height))
                .padding(top = 24.dp, start = 12.dp, end = 12.dp)
        ) { Text("Save") }
        Box(modifier = Modifier.height(24.dp))
    }

}