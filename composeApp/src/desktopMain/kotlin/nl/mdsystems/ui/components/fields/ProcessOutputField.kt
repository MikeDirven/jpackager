package nl.mdsystems.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.mdsystems.model.Process

@Composable
fun ProcessOutputField(
    modifier: Modifier = Modifier,
    process: Process
){
    val processScrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .verticalScroll(processScrollState)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = process.linesRead.joinToString("\n"),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}