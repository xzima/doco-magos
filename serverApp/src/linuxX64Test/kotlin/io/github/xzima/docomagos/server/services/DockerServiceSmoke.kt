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
package io.github.xzima.docomagos.server.services

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.DockerProps
import io.github.xzima.docomagos.server.props.SyncJobProps
import io.github.xzima.docomagos.server.services.impl.DockerClientImpl
import io.github.xzima.docomagos.server.services.impl.DockerServiceImpl
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.*
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * - start docker-compose.yaml
 * - lookup hostname of doco-magos service container
 * - set hostname in [AppProps.hostname]
 */
class DockerServiceSmoke {

    private val appProps = object : AppProps {
        override val hostname: String = ""
        override val staticUiPath: String = "ignore"
        override val jobPeriodMs: Int = 0
        override val ignoreRepoExternalStacksSync: Boolean = true
    }
    private val syncJobProps = object : SyncJobProps {
        override val containerName: String = "doco-magos-sync-job"
        override val containerCmd: String = "sync"
        override val containerAutoRemove: Boolean = true
    }
    private val dockerProps = object : DockerProps {
        override val loggingLevel: LogLevel = LogLevel.ALL
        override val unixSocketFile: String = "/var/run/docker.sock"
    }
    private val dockerService = DockerServiceImpl(
        appProps = appProps,
        syncJobProps = syncJobProps,
        dockerClient = DockerClientImpl(dockerProps),
    )

    /**
     * Scenarios:
     * - hostname not found
     * - container doco-magos-sync-job exist: different image name
     * - container doco-magos-sync-job created -> started
     * - container doco-magos-sync-job exist: already running
     * - container doco-magos-sync-job exist: stopped -> delete -> recreated -> started
     */
    @Test
    @Ignore
    fun runSmoke() = runBlocking {
        KotlinLogging.configureLogging(Level.DEBUG)
        dockerService.tryStartSyncJobService()
    }
}
