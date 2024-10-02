package nl.mdsystems.model

data class Process(
    val started: Boolean = false,
    val finished: Boolean = false,
    val linesRead: List<String> = listOf()
)
