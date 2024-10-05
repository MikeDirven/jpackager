package nl.mdsystems.domain.enums

enum class ConfigurationSteps(val label: String, val packageTypes: List<PackageTypes>, val appImage: Boolean? = null) {
    GENERAL("General", PackageTypes.entries),
    PACKAGE("Package", PackageTypes.entries),
    LAUNCHER("Launcher", PackageTypes.entries, false),
    INSTALLER("Installer", listOf(PackageTypes.EXE, PackageTypes.MSI, PackageTypes.DEB, PackageTypes.RPM, PackageTypes.PKG, PackageTypes.DMG));
}