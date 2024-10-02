package nl.mdsystems

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import nl.mdsystems.model.PackageConfig
import nl.mdsystems.ui.components.fields.ProcessOutputField
import nl.mdsystems.ui.components.overlays.LoadingOverlayWithMessage
import nl.mdsystems.ui.components.snackbars.WixToolsetSnackbar
import nl.mdsystems.ui.screens.PackageConfigScreen
import nl.mdsystems.ui.screens.PackageVariablesScreen
import nl.mdsystems.util.Serializer
import nl.mdsystems.util.isProgramInstalled
import nl.mdsystems.util.wix.installWixToolset
import nl.mdsystems.viewmodel.PackageConfigViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File


@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    viewModel: PackageConfigViewModel = PackageConfigViewModel()
) {
    var checkedForWixToolset by remember { mutableStateOf(false) }
    var wixToolsetAvailable by remember { mutableStateOf(false) }
    var showOverlay by remember { mutableStateOf<String?>(null) }
    var packageConfigState by remember { mutableStateOf(PackageConfig()) }
    val process by viewModel.currentProcess.collectAsState()
    val processScrollState = rememberScrollState()
    var showSavePicker by remember { mutableStateOf(false) }
    var showLoadPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        processScrollState.scrollTo(processScrollState.maxValue)
    }

    LaunchedEffect(Unit){
        if(!checkedForWixToolset){
            showOverlay = "Checking for wix toolset..."
            CoroutineScope(Dispatchers.IO).launch {
                wixToolsetAvailable = isProgramInstalled("candle")
                checkedForWixToolset = true
                showOverlay = null
            }
        }
    }

    DirectoryPicker(
        show = showSavePicker
    ) { folder ->
        File(folder, "${packageConfigState.packageInfo.name}.jpk").apply {
            if(!exists()) createNewFile()
            writeText(
                Serializer().encodeToString(packageConfigState)
            )
        }
        showSavePicker = false
    }

    FilePicker(
        show = showLoadPicker,
        fileExtensions = listOf("jpk")
    ){ file ->
        file?.let {
            packageConfigState = Serializer().decodeFromString(
                File(file.path).readText()
            )
        }
        showLoadPicker = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
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
                        onClick = onExit
                    ){
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
                ){
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = { showSavePicker = !showSavePicker }
                        ){
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save"
                            )
                        }

                        IconButton(
                            onClick = { showLoadPicker = !showLoadPicker }
                        ){
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = "Open"
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.startProcess(packageConfigState)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                        ),
                        enabled = !process.started && wixToolsetAvailable
                    ){
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
    ) { innerPadding ->
        Surface(
            modifier = modifier.padding(innerPadding)
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(8.dp).weight(1f).verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PackageConfigScreen(
                        modifier = Modifier.weight(0.5f),
                        packageConfigInfo = packageConfigState.packageInfo
                    ) {
                        packageConfigState = packageConfigState.copy(packageInfo = it)
                    }

                    PackageVariablesScreen(
                        modifier = Modifier.weight(1f),
                        packageConfigVariables = packageConfigState.packageVariables
                    ) {
                        packageConfigState = packageConfigState.copy(packageVariables = it)
                    }
                }

                if(process.started || process.finished){
                    ProcessOutputField(
                        modifier = Modifier.weight(0.30f),
                        process = process
                    )
                }

                if(!wixToolsetAvailable && checkedForWixToolset) {
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

    LoadingOverlayWithMessage(
        show = showOverlay != null,
        message = showOverlay ?: ""
    )
}