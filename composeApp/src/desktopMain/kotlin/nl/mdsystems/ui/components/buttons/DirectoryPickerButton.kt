package nl.mdsystems.ui.components.buttons

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import java.io.File

@Composable
fun DirectoryPickerButton(
    modifier: Modifier = Modifier,
    directory: String? = null,
    label: String = "Select file",
    onDirectorySelected: (File) -> Unit
) {
    var showPicker by remember { mutableStateOf<Boolean>(false) }

    DirectoryPicker(
        show = showPicker,
        initialDirectory = directory
    ){ path ->
        if (path != null) {
            onDirectorySelected(File(path))
        }
        showPicker = false
    }

    Button(
        modifier = modifier.pointerHoverIcon(PointerIcon.Hand),
        onClick = { showPicker = true }
    ) {
        Text(text = label)
    }
}