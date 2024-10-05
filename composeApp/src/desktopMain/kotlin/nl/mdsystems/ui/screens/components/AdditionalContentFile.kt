package nl.mdsystems.ui.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import nl.mdsystems.domain.enums.FileSizeType
import nl.mdsystems.domain.utils.file.size
import java.io.File

@Composable
fun AdditionalContentFile(
    modifier: Modifier = Modifier,
    file: File,
    onDelete: (File) -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(400.dp),
            overflow = TextOverflow.Ellipsis,
            text = if(file.isFile) file.name else file.absolutePath,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(100.dp),
            text = if(file.isFile) "File" else "Directory",
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(200.dp),
            text = if(file.isFile) "..." else file.walkTopDown().count().toString(),
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(100.dp),
            text = file.size(FileSizeType.MEGABYTES),
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).width(100.dp),
            onClick = {
                onDelete(file)
            }
        ){
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = "Delete"
            )
        }
    }

    Divider()
}