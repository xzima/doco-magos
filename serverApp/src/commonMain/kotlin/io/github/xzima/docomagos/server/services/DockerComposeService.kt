package io.github.xzima.docomagos.server.services

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import io.github.xzima.docomagos.server.services.models.DCListProjects
import io.github.xzima.docomagos.server.services.models.DCVersion
import kotlinx.coroutines.*
import kotlinx.serialization.json.*

class DockerComposeService {

    suspend fun version(): DCVersion = withContext(Dispatchers.IO) {
        val command = Command("docker-compose")
            .args("version", "--format=json")
            .stdout(Stdio.Pipe)
        val child = command.spawn()
        val output = child.waitWithOutput()
        println("version result: $output")
        Json.decodeFromString<DCVersion>(output.stdout!!)
    }

    suspend fun listProjects(): List<DCListProjects> = withContext(Dispatchers.IO) {
        val command = Command("docker-compose")
            .args("ls", "--format=json")
            .stdout(Stdio.Pipe)
        val child = command.spawn()
        val output = child.waitWithOutput()
        //output.status == 0 //isOk
        println("ls result: $output")
        Json.decodeFromString<List<DCListProjects>>(output.stdout!!)
    }
}

