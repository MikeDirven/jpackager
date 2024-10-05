package nl.mdsystems.domain.utils.os

object Os {
    private val currentOs: String = System.getProperty("os.name").uppercase()

    val isWindows
        get() = currentOs.contains("WINDOWS")

    val isLinux
        get() = currentOs.contains("LINUX")

    val isMacOs
        get() = currentOs.contains("MACOS")

    fun isOs(os: String) : Boolean = currentOs.contains(os.uppercase())
}

