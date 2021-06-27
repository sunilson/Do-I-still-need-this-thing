package at.sunilson.doistillneedthisthing.androidApp.presentation.additem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun AddItemPermissions(viewModel: AddItemViewModel = viewModel()) {
    var launchPermissionRequest by rememberSaveable { mutableStateOf(false) }
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    when {
        cameraPermissionState.hasPermission -> viewModel.permissionsGranted()
        cameraPermissionState.shouldShowRationale -> {
            if (doNotShowRationale) {
                viewModel.permissionsDenied()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Permission needed",
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "The camera is important for this app. Please grant the permission.",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) { Text("Request permission") }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        doNotShowRationale = true
                    }) { Text("Don't show rationale again") }
                }
            }
        }
        !cameraPermissionState.permissionRequested -> launchPermissionRequest = true
        else -> viewModel.permissionsDenied()
    }

    // Trigger a side-effect to request the camera permission if it needs to be presented to the user
    if (launchPermissionRequest) {
        LaunchedEffect(cameraPermissionState) {
            cameraPermissionState.launchPermissionRequest()
            launchPermissionRequest = false
        }
    }
}