package io.github.xzima.docomagos.docker.ktor.engine.socket

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.SocketAddress
import io.ktor.network.sockets.SocketOptions
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.tcpNoDelay
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.sync.Semaphore

class ConnectionFactory(
    private val selector: SelectorManager,
    connectionsLimit: Int,
    private val addressConnectionsLimit: Int,
) {
    private val limit = Semaphore(connectionsLimit)
    private val addressLimit = ConcurrentMap<SocketAddress, Semaphore>()

    suspend fun connect(
        address: SocketAddress,
        configuration: SocketOptions.TCPClientSocketOptions.() -> Unit = {},
    ): Socket {
        limit.acquire()
        return try {
            val addressSemaphore = addressLimit.computeIfAbsent(address) { Semaphore(addressConnectionsLimit) }
            addressSemaphore.acquire()

            try {
                aSocket(selector).tcpNoDelay().tcp().connect(address, configuration)
            } catch (cause: Throwable) {
                // a failure or cancellation
                addressSemaphore.release()
                throw cause
            }
        } catch (cause: Throwable) {
            limit.release()
            throw cause
        }
    }

    fun release(address: SocketAddress) {
        addressLimit[address]!!.release()
        limit.release()
    }
}
