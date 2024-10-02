package nl.mdsystems

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import nl.mdsystems.util.isProgramInstalled

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            size = DpSize(1000.dp, 800.dp)
        ),
        title = "jpackager",
        transparent = true,
        undecorated = true,
        icon = rememberVectorPainter(Icons.Default.FolderZip)
    ) {
        var isDragging by remember { mutableStateOf(false) }
        var dragStartPosition by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                .shadow(8.dp, RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitPointerEvent(PointerEventPass.Initial)
                            if (down.changes.any { it.pressed }) {
                                dragStartPosition = down.changes.first().position
                                isDragging = true
                            }

                            while (isDragging) {
                                val event = awaitPointerEvent(PointerEventPass.Main)
                                if (event.changes.any { it.pressed }) {
                                    val currentPosition = event.changes.first().position
                                    val offset = currentPosition - dragStartPosition
                                    this@Window.window.setLocation(
                                        (this@Window.window.x + offset.x).toInt(),
                                        (this@Window.window.y + offset.y).toInt()
                                    )
                                } else {
                                    isDragging = false
                                }
                            }
                        }
                    }
                }
        ) {
            MaterialTheme {
                App(
                    modifier = Modifier,
                    onExit = ::exitApplication
                )
            }
        }
    }
}