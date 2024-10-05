package nl.mdsystems.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import nl.mdsystems.domain.enums.ConfigurationSteps
import nl.mdsystems.domain.utils.os.Os
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.screens.components.GeneralConfig
import nl.mdsystems.ui.screens.components.InstallerConfig
import nl.mdsystems.ui.screens.components.LauncherConfig
import nl.mdsystems.ui.screens.components.PackageConfig
import nl.mdsystems.ui.screens.components.os.windows.WindowsVariables
import nl.mdsystems.util.isEnabled

@Composable
fun configurationScreen(
    modifier: Modifier = Modifier,
    packageConfig: Configuration,
    onConfigChange: (Configuration) -> Unit
){
    var currentTab by remember { mutableStateOf(ConfigurationSteps.GENERAL) }

    fun currentTabStillAvailable() : Boolean {
        return currentTab.packageTypes.contains(packageConfig.packageInfo.type)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabRow(
            selectedTabIndex = if(currentTabStillAvailable()) currentTab.ordinal else ConfigurationSteps.GENERAL.ordinal
        ){
            ConfigurationSteps.entries.forEach {
                Tab(
                    modifier = Modifier.pointerHoverIcon(
                        if(it.isEnabled(packageConfig)) PointerIcon.Hand else PointerIcon.Default
                    ),
                    enabled = it.isEnabled(packageConfig),
                    text = { Text(it.label) },
                    selected = it == currentTab,
                    onClick = { currentTab = it }
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            when(currentTab){
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