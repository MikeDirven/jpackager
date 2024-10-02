package nl.mdsystems.util

import java.io.File

fun isProgramInstalled(programName: String): Boolean {
    val programFilesDir = File("C:\\Program Files") // Windows default
    val programFilesX86Dir = File("C:\\Program Files (x86)") // Windows 32-bit programs on 64-bit OS

    // Search for the program's executable in common installation locations
    return programFilesDir.walkTopDown()
        .any { it.isFile && it.name.startsWith(programName) && it.extension == "exe" }
            || programFilesX86Dir.walkTopDown()
        .any { it.isFile && it.name.startsWith(programName) && it.extension == "exe" }
}