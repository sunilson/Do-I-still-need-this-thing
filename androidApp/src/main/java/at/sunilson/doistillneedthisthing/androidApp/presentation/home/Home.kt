package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import at.sunilson.doistillneedthisthing.androidApp.R

@Composable
fun Home(viewModel: HomeViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addItemClicked() }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add item button")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Test") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                })
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Home", style = MaterialTheme.typography.h1)
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    Home(HomeViewModel())
}