package nl.mdsystems.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.mdsystems.domain.enums.PackageTypes
import nl.mdsystems.model.PackageConfig
import nl.mdsystems.ui.components.fields.TypedEnumSelectField

@Composable
fun PackageConfigScreen(
    packageConfigInfo: PackageConfig.PackageInfo,
    modifier: Modifier = Modifier,
    onConfigChange: (PackageConfig.PackageInfo) -> Unit
){
    var packageConfig by remember { mutableStateOf(PackageConfig.PackageInfo()) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = packageConfigInfo.name.ifEmpty { packageConfig.name },
            singleLine = true,
            onValueChange = {
                packageConfig = packageConfig.copy(name = it)
                onConfigChange(packageConfig)
            },
            label = {
                Text(text = "Package name")
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = packageConfigInfo.appVersion.ifEmpty { packageConfig.appVersion },
            singleLine = true,
            onValueChange = {
                packageConfig = packageConfig.copy(appVersion = it)
                onConfigChange(packageConfig)
            },
            label = {
                Text(text = "Version")
            }
        )

        TypedEnumSelectField(
            modifier = Modifier.fillMaxWidth(),
            initial = packageConfig.type,
            label ="Package type",
            options = PackageTypes.entries,
        ){
            packageConfig = packageConfig.copy(type = it)
            onConfigChange(packageConfig)
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = packageConfigInfo.description.ifEmpty { packageConfig.description },
            onValueChange = {
                packageConfig = packageConfig.copy(description = it)
                onConfigChange(packageConfig)
            },
            label = {
                Text(text = "Description")
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = packageConfigInfo.copyright.ifEmpty { packageConfig.copyright },
            onValueChange = {
                packageConfig = packageConfig.copy(copyright = it)
                onConfigChange(packageConfig)
            },
            label = {
                Text(text = "Copyright")
            }
        )
    }
}