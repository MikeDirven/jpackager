package nl.mdsystems.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.mdsystems.model.PackageConfig
import nl.mdsystems.model.Process

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

    private fun PackageConfig.createCommand() : List<String> {
        return buildList {
            add("jpackage")
            packageInfo.name.takeIf { it.isNotBlank() }?.let {
                add("--name")
                add(it)
            }

            add("--type")
            add(packageInfo.type.name.lowercase())

            add("--app-version")
            add(packageInfo.appVersion)

            packageInfo.icon.takeIf { it.isNotBlank() }?.let {
                add("--icon")
                add(it)
            }

            add("--description")
            add(packageInfo.description)

            add("--copyright")
            add(packageInfo.copyright)

            packageVariables.inputPath?.absolutePath?.let {
                add("--input")
                add(it)
            }

            packageVariables.destinationPath?.absolutePath?.let {
                add("--dest")
                add(it)
            }

            packageVariables.mainJar?.let {
                add("--main-jar")
                add(it)
            }

            packageVariables.mainClass?.let {
                add("--main-class")
                add(it)
            }

            packageVariables.commandLineArgument?.let {
                add("--arguments")
                add(it)
            }

            if(packageVariables.useConsole) add("--win-console")

            if(packageVariables.perUser) add("--win-per-user-install")

            add("--verbose")
        }
    }

    fun startProcess(config: PackageConfig){
        setStartedState()
        viewModelScope.launch(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val process = ProcessBuilder(config.createCommand()).apply {
                redirectErrorStream(true)
            }.start()

            val reader = process.inputStream.reader().buffered()

            // Read output line by line
            reader.forEachLine { line ->
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