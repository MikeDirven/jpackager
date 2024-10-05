package nl.mdsystems.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.mdsystems.domain.utils.os.Os
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.fields.CheckBox
import nl.mdsystems.ui.screens.components.os.windows.WindowsVariables

@Composable
fun InstallerConfig(
    modifier: Modifier = Modifier,
    config: Configuration,
    onConfigChange: (Configuration) -> Unit
){
    CheckBox(
        label = "Install as service",
        checked = config.packageVariables.installAsService,
        onCheck = {
            onConfigChange(
                config.copy(
                    packageVariables = config.packageVariables.copy(
                        installAsService = it
                    )
                )
            )
        }
    )

    if(Os.isWindows) WindowsVariables(
        packageConfig = config,
        onConfigChange = onConfigChange
    )
}