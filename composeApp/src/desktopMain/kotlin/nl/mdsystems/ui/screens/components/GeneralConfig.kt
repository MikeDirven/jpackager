package nl.mdsystems.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.mdsystems.domain.enums.PackageTypes
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.fields.TypedEnumSelectField
import nl.mdsystems.ui.components.pickers.FilePicker
import nl.mdsystems.ui.components.pickers.DirectoryPicker
import nl.mdsystems.util.icoToImageBitmap
import java.io.File

@Composable
fun GeneralConfig(
    modifier: Modifier = Modifier,
    config: Configuration,
    onConfigChange: (Configuration) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageInfo.name,
        singleLine = true,
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageInfo = config.packageInfo.copy(
                        name = it
                    )
                )
            )
        },
        label = {
            Text(text = "Package name")
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageInfo.appVersion,
        singleLine = true,
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageInfo = config.packageInfo.copy(
                        appVersion = it
                    )
                )
            )
        },
        label = {
            Text(text = "Version")
        }
    )

    TypedEnumSelectField(
        modifier = Modifier.fillMaxWidth(),
        selected = config.packageInfo.type,
        label = "Package type",
        options = PackageTypes.osSpecific,
        labelSelector = {
            it.label
        }
    ) {
        onConfigChange(
            config.copy(
                packageInfo = config.packageInfo.copy(
                    type = it
                )
            )
        )
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageInfo.description,
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageInfo = config.packageInfo.copy(
                        description = it
                    )
                )
            )
        },
        label = {
            Text(text = "Description")
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageInfo.vendor,
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageInfo = config.packageInfo.copy(
                        vendor = it
                    )
                )
            )
        },
        label = {
            Text(text = "Vendor")
        }
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.packageInfo.copyright,
        onValueChange = {
            onConfigChange(
                config.copy(
                    packageInfo = config.packageInfo.copy(
                        copyright = it
                    )
                )
            )
        },
        label = {
            Text(text = "Copyright")
        }
    )

    FilePicker(
        modifier = Modifier.fillMaxWidth(),
        initial = config.packageInfo.icon?.absolutePath,
        label = "Icon file",
        extensions = listOf("ico"),
        leadingIcon = config.packageInfo.icon?.let { icoToImageBitmap(it) }
    ){
        onConfigChange(
            config.copy(
                packageInfo = config.packageInfo.copy(
                    icon = it
                )
            )
        )
    }

    DirectoryPicker(
        modifier = Modifier.fillMaxWidth(),
        initial = config.packageVariables.destinationPath?.absolutePath,
        label = "Output folder"
    ) {
        onConfigChange(
            config.copy(
                packageVariables = config.packageVariables.copy(
                    destinationPath = File(it)
                )
            )
        )
    }
}