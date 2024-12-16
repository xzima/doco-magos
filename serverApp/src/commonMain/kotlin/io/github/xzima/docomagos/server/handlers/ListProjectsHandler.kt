/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
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
