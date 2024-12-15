package io.github.xzima.docomagos.client

import io.github.xzima.docomagos.api.ListProjectsResp
import io.github.xzima.docomagos.api.Req
import io.github.xzima.docomagos.api.reqResp
import io.rsocket.kotlin.RSocket

class DockerComposeApiServiceImpl(
    private val client: RSocket,
) : DockerComposeApiService {

    override suspend fun listProjects(): ListProjectsResp {
        return client.reqResp(Req.ListProjects)
    }
}
