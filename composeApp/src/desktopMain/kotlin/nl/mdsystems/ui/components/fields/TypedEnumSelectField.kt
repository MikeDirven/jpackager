package nl.mdsystems.ui.components.fields

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun <T : Enum<T>> TypedEnumSelectField(
    initial: T,
    modifier: Modifier = Modifier,
    label: String = "Select",
    options: List<T>,
    onSelectionChange: (T) -> Unit
) {
    var dropDownState by remember { mutableStateOf(DropdownMenuState(DropdownMenuState.Status.Closed)) }
    var selectedOption by remember { mutableStateOf<T?>(initial) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Row(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth()
                .onGloballyPositioned { coordinates  ->
                    offsetX = coordinates.positionInParent().x
                    offsetY = coordinates.positionInParent().y
                }.onPointerEvent(PointerEventType.Press) {
                    if (it.buttons.isPrimaryPressed) {
                        dropDownState = DropdownMenuState(DropdownMenuState.Status.Open(Offset(offsetX, offsetY)))
                    }
                },
            value = selectedOption?.name ?: "",
            trailingIcon = {
                Icon(
                    imageVector = if(dropDownState.status is DropdownMenuState.Status.Open){
                        Icons.Default.ArrowDropUp
                    } else {
                        Icons.Default.ArrowDropDown
                    },
                    contentDescription = ""
                )

            },
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text(text = label)
            }
        )
        DropdownMenu(dropDownState){
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        dropDownState = DropdownMenuState(DropdownMenuState.Status.Closed)
                        onSelectionChange(option)
                    }
                ){
                    Text(text = option.name)
                }
            }
        }
    }
}