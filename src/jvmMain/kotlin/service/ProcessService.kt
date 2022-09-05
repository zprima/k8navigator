package service

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ProcessService{
    companion object {
        suspend fun run(commands:String, workingDir: File? = null): String{
            val pb = ProcessBuilder()
                .command("bash", "-c", commands)
                .directory(workingDir)
                .redirectErrorStream(true)

            val envs = pb.environment()
            envs["PATH"] = "/opt/homebrew/bin/:/usr/bin:/usr/local/bin"

            val process = pb.start()

            val text = BufferedReader(InputStreamReader(process.inputStream)).readText()
            println(text)

            if(!process.waitFor(15, TimeUnit.SECONDS)){
                process.destroy()
                throw RuntimeException("timeout $text")
            }

            return text
        }
    }
}