package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ContextSwitcher(currentContext:String, contexts: List<String>, onContextClick: (String) -> Unit){
    var showContextDropdown by remember{ mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth().height(50.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("Context:")

        TextButton(onClick = { showContextDropdown = !showContextDropdown }){
            Text(currentContext)
        }
        DropdownMenu(
            expanded = showContextDropdown,
            onDismissRequest = { showContextDropdown = false},
            modifier = Modifier.fillMaxWidth()
        ){
            contexts.forEach {
                DropdownMenuItem(onClick = { onContextClick(it); showContextDropdown = false }){
                    val color = if(currentContext == it) MaterialTheme.colors.primary else Color.Black
                    Text(it, color = color)
                }
            }
        }
    }
}