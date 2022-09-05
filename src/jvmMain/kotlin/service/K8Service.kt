package service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.K8Pod
import model.K8Pods

class K8Service{
    companion object{
        private val jsonSerializer = Json { ignoreUnknownKeys = true }

        suspend fun getK8Contexts(): List<String>{
            val result = ProcessService.run("kubectl config get-contexts -o name")
            return result.split("\n").filter { it.isNotBlank() }
        }

        suspend fun getK8CurrentContext(): String{
            return ProcessService.run("kubectl config current-context").trim()
        }

        suspend fun getK8Pods(): List<K8Pod>{
            val result = ProcessService.run("kubectl get pods -o=json")

            val data = jsonSerializer.decodeFromString<K8Pods>(result)
            return data.items
        }

        suspend fun setK8Context(newContext: String): String{
            return ProcessService.run("kubectl config use-context $newContext")
        }

        suspend fun sshIntoPod(podName: String){
            val podCommand = "kubectl exec -it $podName -- bash"
            val command = "osascript -e 'tell app \"Terminal\" to do script \"$podCommand\"'"
            println(command)

            val builder = ProcessBuilder("bash", "-c", command)
            withContext(Dispatchers.IO) {
                builder.start()
            }

            // user will destroy process when he closes the terminal
        }

        suspend fun showPodLogs(podName: String){
            val podCommand = "kubectl logs $podName"
            val command = "osascript -e 'tell app \"Terminal\" to do script \"$podCommand\"'"
            println(command)

            val builder = ProcessBuilder("bash", "-c", command)
            withContext(Dispatchers.IO) {
                builder.start()
            }

            // user will destroy process when he closes the terminal
        }
    }
}