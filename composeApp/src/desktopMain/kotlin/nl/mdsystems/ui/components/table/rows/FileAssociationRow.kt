package nl.mdsystems.ui.components.table.rows

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import nl.mdsystems.model.Configuration
import nl.mdsystems.util.iconToImageBitmap

@Composable
fun FileAssociationRow(
    modifier: Modifier = Modifier,
    fileAssociation: Configuration.FileAssociation,
    onDelete: (Configuration.FileAssociation) -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.width(100.dp),
            horizontalArrangement = Arrangement.Center
        ){
            fileAssociation.icon?.let { iconToImageBitmap(it) }?.let {
                Image(
                    modifier = Modifier.size(32.dp),
                    bitmap = it,
                    contentDescription = null
                )
            } ?: Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(150.dp),
            overflow = TextOverflow.Ellipsis,
            text = fileAssociation.extension,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(200.dp),
            text = fileAssociation.mimeType,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(400.dp),
            text = fileAssociation.description,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).width(100.dp),
            onClick = {
                onDelete(fileAssociation)
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