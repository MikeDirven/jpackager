package nl.mdsystems.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.mdsystems.model.Configuration
import nl.mdsystems.model.Process
import java.io.File

class PackageConfigViewModel : ViewModel() {
    private val _currentProcess = MutableStateFlow(Process())
    val currentProcess = _currentProcess.asStateFlow()

    private fun setStartedState(){
        _currentProcess.update {
            _currentProcess.value.copy(
                started = true,
                finished = false,
                linesRead = _currentProcess.value.linesRead + "Package creation started"
            )
        }
    }

    private fun setFinished() {
        _currentProcess.update {
            _currentProcess.value.copy(
                started = false,
                finished = true,
                linesRead = _currentProcess.value.linesRead + "Package creation finished"
            )
        }
    }

    private fun setFinishedWitException() {
        _currentProcess.update {
            _currentProcess.value.copy(
                started = false,
                finished = true,
                linesRead = _currentProcess.value.linesRead + "Package creation canceled"
            )
        }
    }

    private fun addOutputLine(line: String){
        _currentProcess.update {
            _currentProcess.value.copy(
                linesRead = _currentProcess.value.linesRead + line
            )
        }
    }

    private fun MutableList<String>.setGeneralConfiguration(config: Configuration) {
        // Start of general configuration
        add("jpackage")

        config.packageInfo.name.takeIf { it.isNotBlank() }?.let {
            add("--name")
            add(it)
        }

        add("--type")
        add(config.packageInfo.type.name.lowercase())

        add("--app-version")
        add(config.packageInfo.appVersion)

        config.packageInfo.icon?.absolutePath?.let {
            add("--icon")
            add("\"$it\"")
        }

        add("--description")
        add(config.packageInfo.description)

        add("--vendor")
        add(config.packageInfo.vendor)

        add("--copyright")
        add(config.packageInfo.copyright)

        config.packageVariables.destinationPath?.absolutePath?.let {
            add("--dest")
            add("\"$it\"")
        }

        config.packageInfo.upgradeCode?.let {
            add("--win-upgrade-uuid")
            add(it)
        }
        // End of general configuration
    }

    private fun MutableList<String>.setPackageConfiguration(config: Configuration) {
        config.packageVariables.inputPath?.absolutePath?.let {
            add("--input")
            add("\"$it\"")
        }

        config.packageVariables.aboutUrl?.let {
            add("--about-url")
            add("\"$it\"")
        }

        config.packageVariables.additionalContent.forEach { file ->
            add("--app-content")
            add("\"${file.absolutePath}\"")
        }
    }

    private fun MutableList<String>.setLauncherConfiguration(config: Configuration) {
        config.packageVariables.mainJar?.let {
            add("--main-jar")
            add(it)
        }

        config.packageVariables.mainClass?.let {
            add("--main-class")
            add(it)
        }

        config.packageVariables.commandLineArgument?.let {
            add("--arguments")
            add(it)
        }

        config.packageVariables.jvmOptions.forEach {
            add("--java-options")
            add("${it.key} ${it.value}")
        }

        if(config.packageVariables.useConsole) add("--win-console")
    }

    private fun MutableList<String>.setInstallerOptions(config: Configuration) {
        // Start of Windows installer options
        if(config.packageVariables.windowsPerUser) add("--win-per-user-install")
        if(config.packageVariables.windowsDirectoryChooser) add("--win-dir-chooser")
        if(config.packageVariables.windowsShortcut) add("--win-shortcut")
        if(config.packageVariables.installAsService) {
            add("--resource-dir")
            add(File("utils").absolutePath)
            add("--launcher-as-service")
        }
    }

    private fun Configuration.createCommand() : List<String> {
        return buildList {
            setGeneralConfiguration(this@createCommand)

            setPackageConfiguration(this@createCommand)

            setLauncherConfiguration((this@createCommand))

            setInstallerOptions(this@createCommand)

            if(verboseLogging) add("--verbose")
        }
    }

    fun clearConsole() {
        _currentProcess.update {
            _currentProcess.value.copy(linesRead = listOf())
        }
    }

    fun startProcess(config: Configuration, setProductCode: (String) -> Unit, setUpgradeCode: (String) -> Unit){
        setStartedState()
        viewModelScope.launch(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val process = ProcessBuilder(config.createCommand()).apply {
                redirectErrorStream(true)
            }.start()

            val reader = process.inputStream.reader().buffered()

            // Read output line by line
            reader.forEachLine { line ->
                when{
                    line.contains("ProductCode:") -> setProductCode(line.substringAfterLast(" ").removeSuffix("."))
                    line.contains("UpgradeCode:") -> setUpgradeCode(line.substringAfterLast(" ").removeSuffix("."))
                }
                addOutputLine(line)
            }

            // Wait for the process to complete
            val exitCode = process.waitFor()

            if(exitCode > 0)
                setFinishedWitException()
            else
                setFinished()
        }
    }
}