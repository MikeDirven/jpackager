package nl.mdsystems.domain.utils.file

import nl.mdsystems.domain.enums.FileSizeType
import java.io.File

fun File.size(type: FileSizeType = FileSizeType.BYTES): String {
    return if(this.isFile){
        this.length()
    } else {
        this.walkTopDown().filter {
            it.isFile
        }.sumOf { it.length() }
    }.let { bytes ->
        when(type){
            FileSizeType.BYTES -> "${bytes}B"
            FileSizeType.KILOBYTES ->"${bytes / 1024}KB"
            FileSizeType.MEGABYTES-> "${bytes / (1024 * 1024)}MB"
            FileSizeType.GIGABYTES -> "${bytes / (1024 * 1024 * 1024)}GB"
        }
    }
}