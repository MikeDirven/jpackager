package nl.mdsystems.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.mdsystems.domain.enums.ConfigurationSteps
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.screens.components.GeneralConfig
import nl.mdsystems.ui.screens.components.InstallerConfig
import nl.mdsystems.ui.screens.components.LauncherConfig
import nl.mdsystems.ui.screens.components.PackageConfig

@Composable
fun configurationScreen(
    modifier: Modifier = Modifier,
    packageConfig: Configuration,
    activeStep: ConfigurationSteps,
    onConfigChange: (Configuration) -> Unit
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            when(activeStep){
                ConfigurationSteps.GENERAL -> {
                    GeneralConfig(
                        config = packageConfig,
                        onConfigChange = onConfigChange
                    )
                }
                ConfigurationSteps.PACKAGE -> {
                    PackageConfig(
                        config = packageConfig,
                        onConfigChange = onConfigChange
                    )
                }
                ConfigurationSteps.LAUNCHER -> {
                    LauncherConfig(
                        config = packageConfig,
                        onConfigChange = onConfigChange
                    )
                }
                ConfigurationSteps.INSTALLER -> {
                    InstallerConfig(
                        config = packageConfig,
                        onConfigChange = onConfigChange
                    )
                }
            }
        }
    }
}