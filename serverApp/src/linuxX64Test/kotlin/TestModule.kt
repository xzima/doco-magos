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
import io.github.xzima.docomagos.koin.configureKoin
import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.env.GitEnv
import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.JobService
import io.github.xzima.docomagos.server.services.PingService
import io.github.xzima.docomagos.server.services.StaticUiService
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.github.xzima.docomagos.server.services.impl.GitServiceImpl
import io.github.xzima.docomagos.server.services.impl.PingServiceImpl

fun initTestKoinModule(port: Int) = configureKoin {
    single {
        RSocketEnv(
            maxFragmentSize = 1024,
        )
    }
    single {
        KtorEnv(
            port = port,
            reuseAddress = true,
            gracePeriodMillis = 0,
            graceTimeoutMillis = 0,
        )
    }
    single {
        AppEnv(
            staticUiPath = "/static/ui/path",
            jobPeriodMs = 3_000,
        )
    }
    single {
        GitEnv(
            mainRepoPath = "/tmp/repo",
            mainRepoUrl = "https://github.com/xzima/home-composes.git",
            mainRepoRemote = "origin",
            mainRepoBranch = "master",
            gitAskPass = "/tmp/GIT_ASKPASS",
            gitToken = null,
            gitTokenFile = null,
        )
    }
    single { StaticUiService(get<AppEnv>()) }
    single { DockerComposeService() }
    single<GitClient> { GitClientImpl(get<GitEnv>().gitAskPass) }
    single<GitService> { GitServiceImpl(get<GitEnv>(), get<GitClient>()) }
    single<PingService> { PingServiceImpl() }
    single { JobService(get<AppEnv>(), get<PingService>(), get<GitService>()) }
    // handlers
    single { ListProjectsHandler(get()) }
}
