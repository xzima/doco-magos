package io.github.xzima.docomagos.server.env

data class KtorEnv(
    val port: Int,
    val reuseAddress: Boolean,
    val gracePeriodMillis: Long,
    val graceTimeoutMillis: Long,
)
