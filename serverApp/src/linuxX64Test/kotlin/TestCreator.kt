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
import io.github.xzima.docomagos.docker.models.RestartPolicy
import io.github.xzima.docomagos.server.services.models.DockerContainerInfo
import io.github.xzima.docomagos.server.services.models.RepoProjectInfo
import okio.Path.Companion.toPath

object TestCreator {

    fun dockerContainerInfo() = DockerContainerInfo(
        id = "any",
        name = null,
        imageName = null,
        hostname = null,
        createdAt = null,
        startedAt = null,
        finishedAt = null,
        status = null,
        restartCount = 0,
        envs = emptyList(),
        labels = emptyMap(),
        binds = emptyList(),
        autoRemove = false,
        restartPolicy = RestartPolicy.Name.UNKNOWN,
    )

    fun repoProjectInfo() = RepoProjectInfo(
        name = "any",
        path = "/tmp/any".toPath(),
        order = Int.MAX_VALUE,
        manifestPath = "/tmp/any/compose.yml".toPath(),
        secretEnvPath = null,
        envPath = null,
    )
}
