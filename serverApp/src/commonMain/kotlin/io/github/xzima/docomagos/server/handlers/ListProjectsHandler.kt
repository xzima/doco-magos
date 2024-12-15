package io.github.xzima.docomagos.server.handlers

import io.github.xzima.docomagos.api.ListProjectsResp
import io.github.xzima.docomagos.api.Req
import io.github.xzima.docomagos.server.services.DockerComposeService

class ListProjectsHandler(
    private val service: DockerComposeService,
) : ReqHandler<Req.ListProjects, ListProjectsResp> {

    override suspend fun handle(request: Req.ListProjects): ListProjectsResp {
        val items = service.listProjects().map {
            ListProjectsResp.ProjectItem(it.name, it.status)
        }
        return ListProjectsResp(items)
    }
}
