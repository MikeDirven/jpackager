package nl.mdsystems.ui.components.snackbars

import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.mdsystems.util.wix.installWixToolset
import java.io.File

@Composable
fun FileExceptionSnackbar(
    modifier: Modifier = Modifier,
    file: File,
    onClose: () -> Unit
){
    Snackbar(
        modifier = modifier,
        action = {
            Button(
                onClick = onClose
            ) {
                Text(text = "Close")
            }
        }
    ) {
        Text(
            text = "File not supported: ${file.absoluteFile}"
        )
    }
}