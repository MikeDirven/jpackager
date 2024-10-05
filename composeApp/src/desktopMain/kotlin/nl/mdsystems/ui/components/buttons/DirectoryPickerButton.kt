package nl.mdsystems.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import java.io.File
import com.darkrockstudios.libraries.mpfilepicker.FilePicker as ComposeFilePicker
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker

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
        modifier = modifier.height(IntrinsicSize.Max).padding(end = 5.dp).pointerHoverIcon(PointerIcon.Hand),
        onClick = { showPicker = true }
    ) {
        Text(text = label)
    }
}