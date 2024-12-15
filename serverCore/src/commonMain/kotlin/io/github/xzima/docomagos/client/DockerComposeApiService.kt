package io.github.xzima.docomagos.client

import io.github.xzima.docomagos.api.ListProjectsResp

interface DockerComposeApiService {

    suspend fun listProjects(): ListProjectsResp
}
