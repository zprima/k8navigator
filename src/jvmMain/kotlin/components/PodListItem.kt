package components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import model.*
import java.util.Calendar

@Composable
@Preview
fun PodListItemPreview(){
    val podMetadata = K8PodMetadata(
        name = "some-long-pod-name-with-random-characters",
        namespace = "fruit-name"
    )

    val podContainerStatus = K8PodContainerStatus(
        image = "some-long-image-name",
        name = "preview-name",
        ready = true,
        restartCount = 0,
        started = true
    )

    val podStatus = K8PodStatus(
        phase = "Running",
        startTime = "2022-08-10T10:59:12",
        containerStatuses = listOf(podContainerStatus)
    )

    val pod = K8Pod(
        metadata = podMetadata,
        status = podStatus,
        spec = null
    )

    PodListItem(
        pod = pod,
        sshCommand = {},
        logsCommand = {}
    )
}

@Composable
fun PodListItem(pod: K8Pod, sshCommand: suspend (String) -> Unit, logsCommand: suspend (String) -> Unit) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .border(1.dp, Color.LightGray)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val podColor = if(pod.status.phase == "Running") Color(0xFF008080) else Color.Black

            SelectionContainer {
                Text(pod.metadata.name, style = TextStyle(color = podColor))
            }

            Row(
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { scope.launch { sshCommand(pod.metadata.name) }}) {
                    Text("ssh")
                }

                TextButton(onClick = { scope.launch { logsCommand(pod.metadata.name) }}) {
                    Text("logs")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${pod.status.phase} in ${pod.metadata.namespace}", fontSize = 12.sp)
            Text(pod.status.startTime, fontSize = 12.sp)
        }
    }
}