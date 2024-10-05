package nl.mdsystems.ui.screens.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.pickers.FilePicker
import nl.mdsystems.util.iconToImageBitmap
import java.io.File

@Composable
fun NewFileAssociationDialog(
  modifier: Modifier = Modifier,
  currentConfig: Configuration,
  onSubmit: (Configuration.FileAssociation) -> Unit,
  onDismiss: () -> Unit
){
  var extensionInput by remember { mutableStateOf<String?>(null) }
  var mimeTypeInput by remember { mutableStateOf<String?>(null) }
  var iconInput by remember {
    mutableStateOf<String?>(currentConfig.packageInfo.icon?.absolutePath)
  }
  var descriptionInput by remember { mutableStateOf<String>("") }

  Dialog(
    onDismissRequest = onDismiss,
  ){
    Card {
      Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ){
        Text(
          text = "New file Association",
          style = MaterialTheme.typography.h6
        )

        Divider()

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = extensionInput ?: "",
          isError = when {
            extensionInput.isNullOrBlank() -> true
            !extensionInput!!.startsWith(".") -> true
            else -> false
          },
          singleLine = true,
          onValueChange = {
            extensionInput = it
          },
          label = {
            Text(text = "Extension")
          }
        )

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = mimeTypeInput ?: "",
          isError = when {
            mimeTypeInput.isNullOrBlank() -> true
            else -> false
          },
          singleLine = true,
          onValueChange = {
            mimeTypeInput = it
          },
          label = {
            Text(text = "Mime type")
          }
        )

        FilePicker(
          modifier = Modifier.fillMaxWidth(),
          initial = iconInput ?: "",
          label = "Icon file",
          extensions = listOf("ico"),
          leadingIcon = iconInput?.let { iconToImageBitmap(File(it)) }
        ){
          iconInput = it.absolutePath
        }

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          value = descriptionInput,
          singleLine = true,
          onValueChange = {
            descriptionInput = it
          },
          label = {
            Text(text = "Description")
          }
        )

        Button(
          modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
          enabled = when {
            extensionInput.isNullOrBlank() -> false
            !extensionInput!!.startsWith(".") -> false
            mimeTypeInput.isNullOrBlank() -> false
            else -> true
          },
          onClick = {
            onSubmit(
              Configuration.FileAssociation(
                extensionInput!!,
                mimeTypeInput!!,
                iconInput?.let{ File(it) },
                descriptionInput
              )
            )
          }
        ) {
          Text(
            text = "Submit"
          )
        }
      }
    }
  }
}