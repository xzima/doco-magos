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
package io.github.xzima.docomagos.server.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.koin.configureKoin
import io.github.xzima.docomagos.koin.inject
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.cli.valuesources.EnvValueSource
import io.github.xzima.docomagos.server.cli.valuesources.YamlValueSource
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.props.AppOptionGroup
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.DockerComposeOptionGroup
import io.github.xzima.docomagos.server.props.DockerComposeProps
import io.github.xzima.docomagos.server.props.DockerOptionGroup
import io.github.xzima.docomagos.server.props.DockerProps
import io.github.xzima.docomagos.server.props.GitOptionGroup
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.props.KtorOptionGroup
import io.github.xzima.docomagos.server.props.KtorProps
import io.github.xzima.docomagos.server.props.RepoStructureOptionGroup
import io.github.xzima.docomagos.server.props.RepoStructureProps
import io.github.xzima.docomagos.server.props.RsocketOptionGroup
import io.github.xzima.docomagos.server.props.RsocketProps
import io.github.xzima.docomagos.server.props.SyncJobOptionGroup
import io.github.xzima.docomagos.server.props.SyncJobProps
import io.github.xzima.docomagos.server.props.customOption
import io.github.xzima.docomagos.server.routes.RouteInjector
import io.github.xzima.docomagos.server.routes.RsocketRouteInjector
import io.github.xzima.docomagos.server.routes.StaticUiRouteInjector
import io.github.xzima.docomagos.server.services.DockerClient
import io.github.xzima.docomagos.server.services.DockerComposeClient
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.DockerService
import io.github.xzima.docomagos.server.services.FileReadService
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.GitCryptClient
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.JobService
import io.github.xzima.docomagos.server.services.PingService
import io.github.xzima.docomagos.server.services.RepoStructureService
import io.github.xzima.docomagos.server.services.SyncProjectService
import io.github.xzima.docomagos.server.services.SyncService
import io.github.xzima.docomagos.server.services.impl.DockerClientImpl
import io.github.xzima.docomagos.server.services.impl.DockerComposeClientImpl
import io.github.xzima.docomagos.server.services.impl.DockerComposeServiceImpl
import io.github.xzima.docomagos.server.services.impl.DockerServiceImpl
import io.github.xzima.docomagos.server.services.impl.FileReadServiceImpl
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.github.xzima.docomagos.server.services.impl.GitCryptClientImpl
import io.github.xzima.docomagos.server.services.impl.GitServiceImpl
import io.github.xzima.docomagos.server.services.impl.JobServiceImpl
import io.github.xzima.docomagos.server.services.impl.PingServiceImpl
import io.github.xzima.docomagos.server.services.impl.RepoStructureServiceImpl
import io.github.xzima.docomagos.server.services.impl.SyncProjectServiceImpl
import io.github.xzima.docomagos.server.services.impl.SyncServiceImpl
import kotlinx.coroutines.*
import org.koin.dsl.bind

private val logger = KotlinLogging.from(AppCommand::class)

class AppCommand(
    configPath: String,
) : CliktCommand() {
    init {
        context {
            autoEnvvarPrefix = null
            valueSources(
                EnvValueSource(),
                YamlValueSource(configPath),
            )
        }
    }

    private val loggingLevel: Level by customOption("logging-level").enum<Level>().required()
    private val rsocketProps: RsocketProps by RsocketOptionGroup()
    private val ktorProps: KtorProps by KtorOptionGroup()
    private val appProps: AppProps by AppOptionGroup()
    private val gitProps: GitProps by GitOptionGroup()
    private val dockerProps: DockerProps by DockerOptionGroup()
    private val syncJobProps: SyncJobProps by SyncJobOptionGroup()
    private val repoStructureProps: RepoStructureProps by RepoStructureOptionGroup()
    private val dockerComposeProps: DockerComposeProps by DockerComposeOptionGroup()

    override fun run(): Unit = runBlocking {
        KotlinLogging.configureLogging(loggingLevel)
        initKoinModule()
        // TODO create check external services task
        inject<DockerComposeClient>().listProjects().also {
            logger.debug { "DC: ${it.size}" }
        }
    }

    fun initKoinModule() = configureKoin {
        single { rsocketProps }
        single { ktorProps }
        single { appProps }
        single { gitProps }
        single { dockerProps }
        single { syncJobProps }
        single { repoStructureProps }
        single { dockerComposeProps }
        single<PingService> { PingServiceImpl() }
        single<DockerComposeClient> { DockerComposeClientImpl(get<DockerComposeProps>().dryRun) }
        single<DockerClient> { DockerClientImpl(get<DockerProps>()) }
        single<DockerService> { DockerServiceImpl(get<AppProps>(), get<SyncJobProps>(), get<DockerClient>()) }
        single<GitClient> { GitClientImpl(get<GitProps>().gitAskPass) }
        single<GitService> { GitServiceImpl(get<GitProps>(), get<GitClient>()) }
        single<RepoStructureService> { RepoStructureServiceImpl(get<RepoStructureProps>()) }
        single<SyncProjectService> { SyncProjectServiceImpl(get<GitProps>()) }
        single<GitCryptClient> { GitCryptClientImpl() }
        single<FileReadService> { FileReadServiceImpl(get<GitCryptClient>()) }
        single<DockerComposeService> { DockerComposeServiceImpl(get<DockerComposeClient>(), get<FileReadService>()) }
        single<SyncService> {
            SyncServiceImpl(
                get<GitProps>(),
                get<AppProps>(),
                get<RepoStructureService>(),
                get<SyncProjectService>(),
                get<DockerComposeService>(),
            )
        }
        single<JobService> {
            JobServiceImpl(
                appEnv = get<AppProps>(),
                pingService = get<PingService>(),
                gitService = get<GitService>(),
                dockerService = get<DockerService>(),
                syncService = get<SyncService>(),
            )
        }
        // routes
        single { StaticUiRouteInjector(get<AppProps>()) } bind RouteInjector::class
        single { RsocketRouteInjector(get<ListProjectsHandler>()) } bind RouteInjector::class
        // handlers
        single { ListProjectsHandler(get<DockerComposeClient>()) }
    }
}
