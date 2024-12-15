package io.github.xzima.docomagos.server

import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.posix.*
import kotlin.concurrent.AtomicReference

val holder = AtomicReference(Job())

@OptIn(ExperimentalForeignApi::class)
fun initGracefulShutdown(): CompletableJob {
    signal(
        SIGINT,
        staticCFunction<Int, Unit> {
            println("start holder complete")
            holder.value.complete()
            println("end holder complete")
        },
    )
    return holder.value
}
