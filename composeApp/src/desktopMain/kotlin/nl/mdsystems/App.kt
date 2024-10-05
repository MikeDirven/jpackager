package nl.mdsystems

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import nl.mdsystems.domain.utils.os.Os
import nl.mdsystems.model.Configuration
import nl.mdsystems.ui.components.fields.CheckBox
import nl.mdsystems.ui.components.fields.ProcessOutputField
import nl.mdsystems.ui.components.overlays.LoadingOverlayWithMessage
import nl.mdsystems.ui.components.snackbars.WixToolsetSnackbar
import nl.mdsystems.ui.screens.configurationScreen
import nl.mdsystems.util.Serializer
import nl.mdsystems.util.isProgramInstalled
import nl.mdsystems.viewmodel.PackageConfigViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File
import java.io.FileOutputStream


@Composable
@Preview
fun App(
    windowScope: FrameWindowScope,
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    viewModel: PackageConfigViewModel = viewModel { PackageConfigViewModel()}
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
    var checkedForWixToolset by remember { mutableStateOf(false) }
    var wixToolsetAvailable by remember { mutableStateOf(false) }
    var checkedForNssm by remember { mutableStateOf(false) }
    var nssmAvailable by remember { mutableStateOf(false) }
    var showOverlay by remember { mutableStateOf<String?>(null) }
    var configurationState by remember { mutableStateOf(Configuration()) }
    val process by viewModel.currentProcess.collectAsState()
    val processScrollState = rememberScrollState()
    var showSavePicker by remember { mutableStateOf(false) }
    var showLoadPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        processScrollState.scrollTo(processScrollState.maxValue)
    }

    LaunchedEffect(Unit) {
        if (Os.isWindows) {
            CoroutineScope(Dispatchers.IO).launch {
                var wixJob: Deferred<Any>? = null
                var nssmJob: Deferred<Any>? = null

                if (!checkedForWixToolset) {
                    wixJob = async {
                        showOverlay = "Checking for wix toolset..."
                        wixToolsetAvailable = isProgramInstalled("candle")
                        checkedForWixToolset = true
                        showOverlay = null
                    }
                }

                wixJob?.await()

                if (!checkedForNssm) {
                    nssmJob = async {
                        showOverlay = "Checking for nssm toolset..."
                        val utilFolder = File("utils")
                        val nssmFile = File("utils/service-installer.exe")

                        if (!utilFolder.exists()) utilFolder.mkdirs()

                        if (!nssmFile.exists()) {
                            showOverlay = "Installing nssm toolset..."
                            nssmFile.apply {
                                createNewFile()
                            }
                            this::class.java.classLoader.getResourceAsStream("installers/service-installer.exe")
                                .use { inputStream ->
                                    nssmFile.outputStream().use { outputStream ->
                                        inputStream.copyTo(outputStream)
                                    }
                                }
                        }

                        showOverlay = null
                        checkedForNssm = true
                        nssmAvailable = true
                    }
                }
            }
        }
    }

    DirectoryPicker(
        show = showSavePicker
    ) { folder ->
        File(folder, "${configurationState.packageInfo.name}.jpk").apply {
            if (!exists()) createNewFile()
            writeText(
                Serializer().encodeToString(configurationState)
            )
        }
        showSavePicker = false
    }

    FilePicker(
        show = showLoadPicker,
        fileExtensions = listOf("jpk")
    ) { file ->
        file?.let {
            configurationState = Serializer().decodeFromString(
                File(file.path).readText()
            )
        }
        showLoadPicker = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.pointerInput(Unit) {
                    awaitEachGesture {
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
                                windowScope.window.setLocation(
                                    (windowScope.window.x + offset.x).toInt(),
                                    (windowScope.window.y + offset.y).toInt()
                                )
                            } else {
                                isDragging = false
                            }
                        }
                    }
                },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = "App icon"
                        )
                        Text(text = "jpackager")
                    }
                },
                windowInsets = WindowInsets(0.dp),
                actions = {
                    IconButton(
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                        onClick = onExit
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                            onClick = { showSavePicker = !showSavePicker }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save"
                            )
                        }

                        IconButton(
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                            onClick = { showLoadPicker = !showLoadPicker }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = "Open"
                            )
                        }

                        if (process.started || process.finished) {
                            Button(
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                onClick = {
                                    viewModel.clearConsole()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.surface,
                                ),
                            ) {
                                Text("Clear console")
                            }
                        }
                    }

                    showOverlay?.let { message ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                color = Color.White
                            )
                            Text(message, color = Color.White)

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    if(wixToolsetAvailable){
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CheckBox(
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                label = "Verbose logging",
                                checked = configurationState.verboseLogging,
                                onCheck = { checked ->
                                    configurationState = configurationState.copy(verboseLogging = checked)
                                }
                            )

                            Button(
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                onClick = {
                                    viewModel.startProcess(
                                        config = configurationState,
                                        setProductCode = { code ->
                                            configurationState = configurationState.copy(
                                                packageInfo = configurationState.packageInfo.copy(
                                                    productCode = code
                                                )
                                            )
                                        },
                                        setUpgradeCode = { code ->
                                            configurationState = configurationState.copy(
                                                packageInfo = configurationState.packageInfo.copy(
                                                    upgradeCode = code
                                                )
                                            )
                                        }
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.surface,
                                ),
                                enabled = !process.started && wixToolsetAvailable
                            ) {
                                if (process.started && !process.finished) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        strokeWidth = 2.dp,
                                    )
                                } else {
                                    Text("Create package")
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier.padding(innerPadding)
        ) {
            Column {
                configurationScreen(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                    packageConfig = configurationState
                ) {
                    configurationState = it
                }

                if (process.started || process.finished) {
                    ProcessOutputField(
                        modifier = Modifier.weight(0.30f),
                        process = process
                    )
                }

                if (!wixToolsetAvailable && checkedForWixToolset) {
                    WixToolsetSnackbar(
                        messageToShow = {
                            showOverlay = it
                        },
                        callBack = {
                            wixToolsetAvailable = it
                        }
                    )
                }
            }
        }
    }
}