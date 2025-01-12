package io.github.xzima.docomagos.server.exceptions

import io.github.xzima.docomagos.docker.infrastructure.HttpResponse
import io.github.xzima.docomagos.docker.models.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url

class DockerApiException(
    val from: Url,
    val status: HttpStatusCode,
    val body: ErrorResponse,
) : RuntimeException("Invalid response from: $from with status: $status, body: $body") {
    companion object {
        suspend fun from(response: HttpResponse<*>) = DockerApiException(
            from = response.from,
            status = response.status,
            body = response.typedBody(),
        )
    }
}
