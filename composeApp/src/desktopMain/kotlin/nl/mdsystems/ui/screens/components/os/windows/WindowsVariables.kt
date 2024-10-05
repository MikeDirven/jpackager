package nl.mdsystems.ui.screens.components.os.windows

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.mdsystems.domain.enums.PackageTypes
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.fields.CheckBox

@Composable
fun WindowsVariables(
    modifier: Modifier = Modifier,
    packageConfig: Configuration,
    onConfigChange: (Configuration) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = packageConfig.packageVariables.useConsole,
            onCheckedChange = {
                onConfigChange(
                    packageConfig.copy(
                        packageVariables = packageConfig.packageVariables.copy(
                            useConsole = it
                        )
                    )
                )
            }
        )

        Text("Use console (Windows)")
    }

    if(packageConfig.packageInfo.type in listOf(PackageTypes.EXE, PackageTypes.MSI)){
        CheckBox(
            label = "Per user install",
            checked = packageConfig.packageVariables.windowsPerUser,
            onCheck = {
                onConfigChange(
                    packageConfig.copy(
                        packageVariables = packageConfig.packageVariables.copy(
                            windowsPerUser = it
                        )
                    )
                )
            }
        )

        CheckBox(
            label = "Directory chooser",
            checked = packageConfig.packageVariables.windowsDirectoryChooser,
            onCheck = {
                onConfigChange(
                    packageConfig.copy(
                        packageVariables = packageConfig.packageVariables.copy(
                            windowsDirectoryChooser = it
                        )
                    )
                )
            }
        )

        CheckBox(
            label = "Shortcut",
            checked = packageConfig.packageVariables.windowsShortcut,
            onCheck = {
                onConfigChange(
                    packageConfig.copy(
                        packageVariables = packageConfig.packageVariables.copy(
                            windowsShortcut = it
                        )
                    )
                )
            }
        )
    }
}