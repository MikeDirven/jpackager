package nl.mdsystems.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.io.File
import javax.imageio.ImageIO

fun iconToImageBitmap(file: File): ImageBitmap? = try {
    var returnImage: ImageBitmap? = null
    ImageIO.scanForPlugins()
    val imageInputStream = ImageIO.createImageInputStream(file)
    val readers = ImageIO.getImageReadersByFormatName("ico")
    if (readers.hasNext()) {
        val reader = readers.next()
        reader.input = imageInputStream
        val image = reader.read(0) // Read the first image in the ICO file
        returnImage = image.toComposeImageBitmap()
    }

    returnImage
} catch (e: Exception){
    e.printStackTrace()
    println("Failed to convert icon")
    null
}