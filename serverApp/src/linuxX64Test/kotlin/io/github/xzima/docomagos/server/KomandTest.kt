package io.github.xzima.docomagos.server

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

@Ignore
@FlowPreview
class KomandTest {


    @Test
    fun testKomand() = runBlocking {
        getForever(getStats())
            .onCompletion { println("Completion: $it") }
            .timeout(10.seconds)
            .collect { println(it) }
    }

    private suspend fun getContainers() = withContext(Dispatchers.IO) {
        val c = Command("docker-compose").args("-p", "docker", "ps", "--format=json")
            .stdout(Stdio.Pipe).spawn()
        c.waitWithOutput()
    }

    private fun <T> getForever(source: Flow<T>) = flow {
        var i = 0
        do {
            emitAll(source)
            println("retry: $i")
            delay(1000)
            i++
        } while (true)
    }

    private fun getStats() = flow {
        val c = Command("docker-compose").args("-p", "docker", "stats", "--format=json")
            .stdout(Stdio.Pipe).spawn()
        c.bufferedStdout()?.lines()?.forEach {
            emit(it)
        }
    }

    private fun getLogs() = flow {
        val c = Command("docker-compose").args("-p", "docker", "logs", "--follow")
            .stdout(Stdio.Pipe).spawn()
        c.bufferedStdout()?.lines()?.forEach {
            emit(it)
        }
    }
}
