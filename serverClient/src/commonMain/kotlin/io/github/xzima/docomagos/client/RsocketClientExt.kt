package io.github.xzima.docomagos.client

import io.github.xzima.docomagos.Constants
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.ktor.client.RSocketSupport
import io.rsocket.kotlin.ktor.client.rSocket
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data

suspend fun createRsocketClient(host: String, protocol: String = "ws"): RSocket {
    // create ktor client
    val client = HttpClient {
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
    return client.rSocket("$protocol://$host/${Constants.RSOCKET_API_PATH}")

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
