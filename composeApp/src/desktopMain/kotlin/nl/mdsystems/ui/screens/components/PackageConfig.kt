package nl.mdsystems.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.buttons.DirectoryPickerButton
import nl.mdsystems.ui.components.buttons.FilePickerButton
import nl.mdsystems.ui.components.fields.CheckBox
import nl.mdsystems.ui.components.pickers.DirectoryPicker
import java.io.File


@Composable
fun PackageConfig(
    modifier: Modifier = Modifier,
    config: Configuration,
    onConfigChange: (Configuration) -> Unit
){
    DirectoryPicker(
        modifier = Modifier.fillMaxWidth(),
        initial = config.packageVariables.inputPath?.absolutePath,
        label = "Input folder"
    ) {
        onConfigChange(
            config.copy(
                packageVariables = config.packageVariables.copy(
                    inputPath = File(it), mainJar = null, mainClass = null
                )
            )
        )
    }

    CheckBox(
        label = "Input is app image",
        checked = config.packageVariables.inputIsAppImage,
        onCheck = {
            onConfigChange(
                config.copy(
                    packageVariables = config.packageVariables.copy(
                        inputIsAppImage = it
                    )
                )
            )
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageVariables.aboutUrl ?: "",
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageVariables = config.packageVariables.copy(
                        aboutUrl = it
                    )
                )
            )
        },
        label = {
            Text(text = "About url")
        }
    )

    Divider()

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Additional content",
            style = MaterialTheme.typography.h5
        )

        Row {
            FilePickerButton(
                label = "Add file"
            ) { file ->
                onConfigChange(
                    config.copy(
                        packageVariables = config.packageVariables.copy(
                            additionalContent = config.packageVariables.additionalContent + file
                        )
                    )
                )
            }

            DirectoryPickerButton(
                label = "Add directory"
            ) { directory ->
                onConfigChange(
                    config.copy(
                        packageVariables = config.packageVariables.copy(
                            additionalContent = config.packageVariables.additionalContent + directory
                        )
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        AdditionalContentFilesHeader()

        config.packageVariables.additionalContent.forEach { additionalFile ->
            AdditionalContentFile(
                file = additionalFile,
                onDelete = {
                    onConfigChange(
                        config.copy(
                            packageVariables = config.packageVariables.copy(
                                additionalContent = config.packageVariables.additionalContent - additionalFile
                            )
                        )
                    )
                }
            )
        }
    }

}