package nl.mdsystems.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.fields.TypedSelectField
import java.io.File
import java.util.jar.JarFile

@Composable
fun LauncherConfig(
    modifier: Modifier = Modifier,
    config: Configuration,
    onConfigChange: (Configuration) -> Unit
){
    var jvmKeyInput by remember { mutableStateOf("") }
    var jvmValueInput by remember { mutableStateOf("") }

    TypedSelectField(
        label = "Main jar file",
        options = config.packageVariables.inputPath?.listFiles { dir, name -> name.contains("jar") }?.toList() ?: listOf(),
        labelGetter = File::nameWithoutExtension,
        initial = config.packageVariables.mainJar?.let { File(it) },
        onSelectionChange = { selection ->
            onConfigChange(
                config.copy(
                    packageVariables = config.packageVariables.copy(
                        mainJar = config.packageVariables.inputPath?.absolutePath?.let {
                            selection.absolutePath.replace(it, "").removePrefix("\\")
                        },
                        mainClass = JarFile(selection).manifest.mainAttributes.getValue("Main-Class")
                    )
                )
            )
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageVariables.mainClass ?: "",
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageVariables = config.packageVariables.copy(
                        mainClass = it
                    )
                )
            )
        },
        label = {
            Text(text = "Main class")
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageVariables.commandLineArgument ?: "",
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageVariables = config.packageVariables.copy(
                        commandLineArgument = it
                    )
                )
            )
        },
        label = {
            Text(text = "Main class arguments")
        }
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(0.5f),
            value = jvmKeyInput,
            placeholder = {
                Text(text = "example: -option")
            },
            onValueChange = {
                jvmKeyInput = if(it.startsWith("-")) it else "--$it"
            },
            label = {
                Text(text = "jvm option")
            }
        )

        OutlinedTextField(
            modifier = Modifier.weight(0.5f),
            value = jvmValueInput,
            onValueChange = {
                jvmValueInput = it
            },
            label = {
                Text(text = "Option value")
            }
        )

        Button(
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            onClick = {
                onConfigChange(
                    config.copy(
                        packageVariables = config.packageVariables.copy(
                            jvmOptions = config.packageVariables.jvmOptions + (jvmKeyInput to jvmValueInput)
                        )
                    )
                )
                jvmKeyInput = ""
                jvmValueInput = ""
            }
        ) {
           Text(
               text = "Add jvm option"
           )
        }
    }

    Divider(
        modifier.padding(vertical = 8.dp)
    )

    config.packageVariables.jvmOptions.forEach {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${it.key} = ${it.value}",
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Button(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                onClick = {
                    onConfigChange(
                        config.copy(
                            packageVariables = config.packageVariables.copy(
                                jvmOptions = config.packageVariables.jvmOptions - it.key
                            )
                        )
                    )
                }
            ) {
                Text(
                    text = "Remove"
                )
            }
        }
    }
}