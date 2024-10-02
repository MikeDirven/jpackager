package nl.mdsystems.domain.enums

import kotlinx.serialization.Serializable

@Serializable
enum class PackageTypes {
    `APP-IMAGE`,
    EXE,
    MSI,
    RPM,
    DEB,
    PKG,
    DMG
}