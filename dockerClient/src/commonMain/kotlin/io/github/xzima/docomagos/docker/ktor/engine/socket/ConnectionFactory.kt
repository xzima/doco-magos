/**
 * Copyright 2025 Alex Zima(xzima@ro.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
