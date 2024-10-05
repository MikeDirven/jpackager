package nl.mdsystems.ui.components.table.headers

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileAssociationsHeader(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(100.dp),
            text = "Icon",
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(150.dp),
            text = "Extension",
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(200.dp),
            text = "Mime type",
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(400.dp),
            text = "Description",
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.width(100.dp),
            text = "Actions",
            style = MaterialTheme.typography.h6
        )
    }

    Divider()
}