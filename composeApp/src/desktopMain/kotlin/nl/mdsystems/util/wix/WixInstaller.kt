package nl.mdsystems.util.wix

import java.io.File
import java.io.FileOutputStream

fun installWixToolset() : Boolean {
    // Get the .exe file from resources
    val exeFile = ::installWixToolset::class.java.getResourceAsStream("/installers/wix314.exe")

    // Create a temporary file to store the .exe
    val tempFile = File.createTempFile("temp", ".exe")
    tempFile.deleteOnExit()

    // Copy the .exe from resources to the temporary file
    FileOutputStream(tempFile).use { outputStream ->
        exeFile?.copyTo(outputStream)
    }

    // Make the temporary file executable
    tempFile.setExecutable(true)

    // Run the .exe
    val processBuilder = ProcessBuilder(tempFile.absolutePath)
    val installProcess = processBuilder.start()

    return installProcess.waitFor() == 0
}