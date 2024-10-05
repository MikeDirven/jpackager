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

@Composable
fun FilePickerButton(
    modifier: Modifier = Modifier,
    directory: String? = null,
    extensions: List<String> = listOf(),
    label: String = "Select file",
    onFileSelected: (File) -> Unit
) {
    var showPicker by remember { mutableStateOf<Boolean>(false) }

    ComposeFilePicker(
        show = showPicker,
        fileExtensions = extensions,
        initialDirectory = directory
    ) { file ->
        file?.let {
            onFileSelected(File(file.path))
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