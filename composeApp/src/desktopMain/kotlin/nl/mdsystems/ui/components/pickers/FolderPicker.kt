package nl.mdsystems.ui.components.pickers

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker

@Composable
fun FolderPicker(
    modifier: Modifier = Modifier,
    initial: String?,
    label: String = "Select folder",
    onDirectorySelected: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf<Boolean>(false) }
    var selectedDirectory by remember { mutableStateOf<String>("") }

    DirectoryPicker(showPicker){ path ->
        if (path != null) {
            selectedDirectory = path
            onDirectorySelected(path)
        }
        showPicker = false
    }

    Row(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = initial ?: selectedDirectory,
            readOnly = true,
            onValueChange = {},
            singleLine = true,
            trailingIcon = {
                Button(
                    modifier = Modifier.height(IntrinsicSize.Max).padding(end = 5.dp),
                    onClick = { showPicker = true }
                ){
                    Text(text = "Select Folder")
                }
            },
            label = {
                Text(text = label)
            }
        )
    }
}