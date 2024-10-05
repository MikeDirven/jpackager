package nl.mdsystems.ui.components.pickers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import com.darkrockstudios.libraries.mpfilepicker.FilePicker as ComposeFilePicker
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun FilePicker(
    modifier: Modifier = Modifier,
    directory: String? = null,
    initial: String? = null,
    leadingIcon: ImageBitmap? = null,
    extensions: List<String> = listOf(),
    label: String = "Select file",
    onFileSelected: (File) -> Unit
) {
    var showPicker by remember { mutableStateOf<Boolean>(false) }
    var selectedFile by remember { mutableStateOf<String>("") }

    ComposeFilePicker(
        show = showPicker,
        fileExtensions = extensions,
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
            value = initial ?: selectedFile,
            readOnly = true,
            onValueChange = {},
            singleLine = true,
            leadingIcon = leadingIcon?.let {
                {
                    Image(
                        modifier = Modifier.size(32.dp),
                        bitmap = it,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                Button(
                    modifier = Modifier.height(IntrinsicSize.Max).padding(end = 5.dp).pointerHoverIcon(PointerIcon.Hand),
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