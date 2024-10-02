package nl.mdsystems.ui.components.snackbars

import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.mdsystems.util.wix.installWixToolset

@Composable
fun WixToolsetSnackbar(
    modifier: Modifier = Modifier,
    messageToShow: (String?) -> Unit,
    callBack: (Boolean) -> Unit
){
    Snackbar(
        modifier = modifier,
        action = {
            Button(
                onClick = {
                    messageToShow("Installing wix toolset...")
                    CoroutineScope(Dispatchers.IO).launch {
                        val installed = installWixToolset()
                        callBack(installed)
                        messageToShow(null)
                    }
                }
            ) {
                Text(text = "Install Wix toolset")
            }
        }
    ) {
        Text(
            text = "Wix toolset is missing, packaging will not work!"
        )
    }
}