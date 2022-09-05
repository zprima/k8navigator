package components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                .height(300.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            if (isLoadingPods) {
                CircularProgressIndicator()
            } else {
                pods.forEach { pod ->
                    Column(Modifier.fillMaxWidth().padding(bottom = 16.dp).border(1.dp, Color.Red)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(pod.metadata.name)

                            Row(
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { scope.launch { sshCommand(pod.metadata.name) }}) {
                                    Text("ssh")
                                }

                                TextButton(onClick = { scope.launch { logsCommand(pod.metadata.name) }}) {
                                    Text("logs")
                                }

                                TextButton(onClick = { }) {
                                    Text("more")
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(pod.metadata.namespace)
                            Text(pod.status.phase)
                            Text(pod.status.startTime)
                        }
                    }
                }
            }
        }
    }
}