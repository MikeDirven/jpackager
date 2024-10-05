package nl.mdsystems.domain.enums

import kotlinx.serialization.Serializable
import nl.mdsystems.domain.utils.os.Os

@Serializable
enum class PackageTypes(val label: String, val operationSystem: String? = null) {
    `APP-IMAGE`("App image"),
    EXE("Exe installer", "Windows"),
    MSI("Msi installer", "Windows"),
    RPM("Rpm package", "Linux"),
    DEB("Deb package", "Linux"),
    PKG("Pkg package", "Linux"),
    DMG("Dmg package", "macOS");

    companion object {
        val osSpecific: List<PackageTypes>
            get() {
                return PackageTypes.entries.filter {
                    it.operationSystem?.uppercase()?.let { osName ->
                        Os.isOs(osName)
                    } ?: true
                }
            }
    }
}