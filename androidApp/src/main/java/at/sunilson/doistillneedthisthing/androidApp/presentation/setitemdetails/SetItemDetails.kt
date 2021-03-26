package at.sunilson.doistillneedthisthing.androidApp.presentation.setitemdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.sunilson.doistillneedthisthing.androidApp.R

@Composable
fun SetItemDetails() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .background(Color(0xFFEDEDED), shape = RoundedCornerShape(2.dp))
                .width(60.dp)
                .height(4.dp)
        )
        OutlinedTextField(
            value = TextFieldValue("Test123"),
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {}
        )
        OutlinedTextField(
            value = TextFieldValue("Test123"),
            label = { Text("Location") },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            onValueChange = {}
        )
        OutlinedTextField(
            value = TextFieldValue("Test123"),
            label = { Text("Note") },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            onValueChange = {}
        )
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.primary_button_height))
                .padding(top = 24.dp)
        ) {
            Text("Save")
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SetItemDetailsPreview() {
    SetItemDetails()
}