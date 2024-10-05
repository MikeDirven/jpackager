package nl.mdsystems.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import nl.mdsystems.domain.enums.PackageTypes
import java.io.File

@Serializable
data class Configuration(
    val packageInfo : PackageInfo = PackageInfo(),
    val packageVariables: PackageVariables = PackageVariables(),

    val useConsole: Boolean = false,
    val verboseLogging: Boolean = false
) {
    @Serializable
    data class PackageInfo(
        val name: String = "",
        val icon: @Contextual File? = null,
        val type: PackageTypes = PackageTypes.`APP-IMAGE`,
        val appVersion: String = "1.0",
        val vendor: String = "",
        val copyright: String = "",
        val description: String = "",

        val productCode: String? = null,
        val upgradeCode: String? = null
    )

    @Serializable
    data class PackageVariables(
        val destinationPath: @Contextual File? = null,
        val inputPath: @Contextual File? = null,
        val inputIsAppImage: Boolean = false,
        val optionalFiles: List<@Contextual File> = emptyList(),
        val installDirectory: @Contextual File? = null,
        val additionalContent: List<@Contextual File> = emptyList(),

        val aboutUrl: String? = null,

        val commandLineArgument: String? = null,
        val jvmOptions: Map<String, String> = emptyMap(),
        val mainClass: String? = null,
        val mainJar: String? = null,
        val useConsole: Boolean = false,
        val fileAssociation: List<FileAssociation> = emptyList(),

        // General installer options
        val installAsService: Boolean = false,

        // Windows installer options
        val windowsPerUser: Boolean = false,
        val windowsDirectoryChooser: Boolean = false,
        val windowsShortcut: Boolean = false
    )

    @Serializable
    data class FileAssociation(
        val extension: String,
        val mimeType: String,
        val icon: @Contextual File?,
        val description: String
    )
}
