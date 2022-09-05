import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.K8Pod
import service.K8Service

@Composable
fun App() {
    val contexts = remember{ mutableStateListOf<String>() }
    var currentContext by remember{ mutableStateOf("") }
    val pods = remember{ mutableStateListOf<K8Pod>() }
    var isLoadingPods by remember{ mutableStateOf(false) }
    var showContextDropdown by remember{ mutableStateOf(false) }
    // val commandHistory = remember{ MutableStateFlow<List<String>>(emptyList()) }

    suspend fun getContexts(){
        val results = K8Service.getK8Contexts()

        contexts.clear()
        contexts.addAll(results)
    }

    suspend fun getCurrentContext(){
        val result = K8Service.getK8CurrentContext()
        currentContext = result
    }

    suspend fun getPods(){
        isLoadingPods = true
        val results = K8Service.getK8Pods()
        pods.clear()
        pods.addAll(results)
        isLoadingPods = false
    }

    suspend fun setContext(newContext: String){
        val result = K8Service.setK8Context(newContext)
        currentContext = newContext
    }

    suspend fun sshIntoPod(podName: String){
        K8Service.sshIntoPod(podName = podName)
    }

    suspend fun showLogs(podName: String){
        K8Service.showPodLogs(podName = podName)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        scope.launch {
            getCurrentContext()
            getContexts()
        }
    }

    MaterialTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues).padding(8.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Context selection
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Context:")

                    TextButton(onClick = { showContextDropdown = !showContextDropdown }){
                        Text(currentContext)
                    }
                    DropdownMenu(expanded = showContextDropdown, onDismissRequest = { showContextDropdown = false}, modifier = Modifier.fillMaxWidth()){
                        contexts.forEach {
                            DropdownMenuItem(onClick = { scope.launch { setContext(it)}; showContextDropdown = false }){
                                val color = if(currentContext == it) MaterialTheme.colors.primary else Color.Black
                                Text(it, color = color)
                            }
                        }
                    }
                }
                // END Context selection

                // Pod list
                Column() {
                    Button(onClick = {
                        scope.launch(Dispatchers.IO) {
                            getPods()
                        }
                    }) {
                        Text("Get Pods")
                    }
                    Column(Modifier.padding(8.dp).verticalScroll(state = rememberScrollState())) {
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
                                            TextButton(onClick = { scope.launch { sshIntoPod(pod.metadata.name) } }) {
                                                Text("ssh")
                                            }

                                            TextButton(onClick = { scope.launch { showLogs(pod.metadata.name) } }) {
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
                // END Pod list

                // Command history
                Column(
                    modifier = Modifier.background(Color.LightGray),
                    verticalArrangement = Arrangement.Bottom,
                ){

                    Text("Command 1")
                    Text("Command 2")
                    Text("Command 3")

                }
                // END Command history
            }
        }
    }
}