package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.K8Pod

@Composable
fun PodList(
    getPods: suspend () -> Unit,
    isLoadingPods: Boolean,
    pods: List<K8Pod>,
    sshCommand: suspend (String) -> Unit,
    logsCommand: suspend (String) -> Unit) {

    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }

    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { scope.launch(Dispatchers.IO) { getPods() } }) {
                Text("Get Pods")
            }

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                placeholder = { Text("Filter pods by name")},
                shape = RectangleShape,
                modifier = Modifier.background(Color.White),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
                .verticalScroll(state = rememberScrollState(), enabled = true)
        ) {
            if (isLoadingPods) {
                CircularProgressIndicator()
            } else {
                pods.filter { pod -> if(searchText.length > 0) pod.metadata.name.contains(searchText, ignoreCase = true) else true }.forEach { pod ->
                    PodListItem(
                        pod = pod,
                        sshCommand = sshCommand,
                        logsCommand = logsCommand
                    )
                }
            }
        }
    }
}