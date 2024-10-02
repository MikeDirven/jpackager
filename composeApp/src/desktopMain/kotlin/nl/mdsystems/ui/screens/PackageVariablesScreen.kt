package nl.mdsystems.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.dp
import nl.mdsystems.domain.enums.PackageTypes
import nl.mdsystems.model.PackageConfig
import nl.mdsystems.ui.components.fields.TypedSelectField
import nl.mdsystems.ui.components.pickers.FilePicker
import nl.mdsystems.ui.components.pickers.FolderPicker
import java.io.File
import java.util.jar.JarFile

@Composable
fun PackageVariablesScreen(
    modifier: Modifier = Modifier,
    packageConfigVariables: PackageConfig.PackageVariables,
    onConfigChange: (PackageConfig.PackageVariables) -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FolderPicker(
            modifier = Modifier.fillMaxWidth(),
            initial = packageConfigVariables.inputPath?.absolutePath,
            label = "Input folder"
        ) {
            onConfigChange(packageConfigVariables.copy(inputPath = File(it), mainJar = null, mainClass = null))
        }

        FolderPicker(
            modifier = Modifier.fillMaxWidth(),
            initial = packageConfigVariables.destinationPath?.absolutePath,
            label = "Output folder"
        ) {
            onConfigChange(packageConfigVariables.copy(destinationPath = File(it)))
        }

        TypedSelectField(
            label = "Main jar file",
            options = packageConfigVariables.inputPath?.listFiles { dir, name -> name.contains("jar") }?.toList() ?: listOf(),
            labelGetter = File::nameWithoutExtension,
            initial = packageConfigVariables.mainJar?.let { File(it) },
            onSelectionChange = { selection ->
                onConfigChange(packageConfigVariables.copy(
                    mainJar = packageConfigVariables.inputPath?.absolutePath?.let {
                        selection.absolutePath.replace(it, "").removePrefix("\\")
                    },
                    mainClass = JarFile(selection).manifest.mainAttributes.getValue("Main-Class")
                ))
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = packageConfigVariables.mainClass ?: "",
            onValueChange = {
                onConfigChange(packageConfigVariables.copy(mainClass = it))
            },
            label = {
                Text(text = "Main class")
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = packageConfigVariables.commandLineArgument ?: "",
            onValueChange = {
                onConfigChange(packageConfigVariables.copy(commandLineArgument = it))
            },
            label = {
                Text(text = "Main class arguments")
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = packageConfigVariables.useConsole,
                onCheckedChange = {
                    onConfigChange(packageConfigVariables.copy(useConsole = it))
                }
            )

            Text("Use console (Windows)")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = packageConfigVariables.perUser,
                onCheckedChange = {
                    onConfigChange(packageConfigVariables.copy(perUser = it))
                }
            )

            Text("Per user install (Windows)")
        }
    }
}