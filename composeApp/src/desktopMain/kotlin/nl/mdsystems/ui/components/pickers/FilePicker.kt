package nl.mdsystems.ui.components.pickers

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import com.darkrockstudios.libraries.mpfilepicker.FilePicker as ComposeFilePicker

@Composable
fun FilePicker(
    modifier: Modifier = Modifier,
    directory: String? = null,
    label: String = "Select file",
    onFileSelected: (File) -> Unit
) {
    var showPicker by remember { mutableStateOf<Boolean>(false) }
    var selectedFile by remember { mutableStateOf<String>("") }

    ComposeFilePicker(
        show = showPicker,
        initialDirectory = directory
    ){ file ->
        file?.let {
            selectedFile = file.path
            onFileSelected(File(file.path))
        }
        showPicker = false
    }

    Row(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = selectedFile,
            readOnly = true,
            onValueChange = {},
            singleLine = true,
            trailingIcon = {
                Button(
                    modifier = Modifier.height(IntrinsicSize.Max).padding(end = 5.dp),
                    onClick = { showPicker = true }
                ){
                    Text(text = "Select File")
                }
            },
            label = {
                Text(text = label)
            }
        )
    }
}