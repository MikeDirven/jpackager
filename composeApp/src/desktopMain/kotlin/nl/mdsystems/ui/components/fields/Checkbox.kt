package nl.mdsystems.ui.components.fields

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    label: String = "",
    checked: Boolean,
    onCheck: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            checked = checked,
            onCheckedChange = onCheck
        )

        Text(label)
    }
}