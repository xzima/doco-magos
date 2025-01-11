/**
 * Copyright 2024-2025 Alex Zima(xzima@ro.ru)
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
package io.github.xzima.docomagos.client

import io.github.xzima.docomagos.RsocketConst
import io.github.xzima.docomagos.logging.HttpClientLogger
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.ktor.client.RSocketSupport
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data

suspend fun <T : HttpClientEngineConfig> createRsocketClient(
    engineFactory: HttpClientEngineFactory<T>,
    host: String,
    reqRespLogLevel: LogLevel,
): RSocket {
    // create ktor client
    val client = HttpClient(engineFactory) {
        install(Logging) {
            level = reqRespLogLevel
            logger = HttpClientLogger()
        }
        install(WebSockets) // rsocket requires websockets plugin installed
        install(RSocketSupport) {
            // configure rSocket connector (all values have defaults)
            connector {
//                maxFragmentSize = 1024

                connectionConfig {
//                    keepAlive = KeepAlive(
//                        interval = 30.seconds,
//                        maxLifetime = 2.minutes,
//                    )

                    // payload for setup frame
                    setupPayload { buildPayload { data("Hello from frontend!") } }

                    // mime types
//                    payloadMimeType = PayloadMimeType(
//                        data = WellKnownMimeType.ApplicationJson,
//                        metadata = WellKnownMimeType.MessageRSocketCompositeMetadata,
//                    )
                }

                // optional acceptor for server requests
//                acceptor {
//                    RSocketRequestHandler {
//                        requestResponse { it } // echo request payload
//                    }
//                }
            }
        }
    }

// connect to some url
    return client.rSocket("${RsocketConst.PROTOCOL}://$host/${RsocketConst.PATH}")

// request stream
//    val stream: Flow<Payload> = rSocket.requestStream(
//        buildPayload {
//            data("""{ "data": "hello world" }""")
//        },
//    )

// take 5 values and print response
//    stream.take(5).collect { payload: Payload ->
//        println(payload.data.readText())
//    }
}
