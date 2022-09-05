package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    Column() {
        Button(onClick = { scope.launch(Dispatchers.IO) { getPods() }}) {
            Text("Get Pods")
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
                pods.forEach { pod ->
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