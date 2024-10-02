package nl.mdsystems.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import nl.mdsystems.domain.enums.PackageTypes
import java.io.File

@Serializable
data class PackageConfig(
    val packageInfo : PackageInfo = PackageInfo(),
    val packageVariables: PackageVariables = PackageVariables(),

    val useConsole: Boolean = false
) {
    @Serializable
    data class PackageInfo(
        val name: String = "",
        val icon: String = "",
        val type: PackageTypes = PackageTypes.`APP-IMAGE`,
        val appVersion: String = "1.0",
        val copyright: String = "",
        val description: String = "",
    )

    @Serializable
    data class PackageVariables(
        val destinationPath: @Contextual File? = null,
        val inputPath: @Contextual File? = null,
        val optionalFiles: List<@Contextual File> = emptyList(),

        val commandLineArgument: String? = null,
        val jvmOptions: List<String> = emptyList(),
        val mainClass: String? = null,
        val mainJar: String? = null,
        val useConsole: Boolean = false,
        val perUser: Boolean = false
    )
}
