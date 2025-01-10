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
import com.github.ajalt.clikt.parameters.options.option
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
import io.github.xzima.docomagos.server.props.GitOptionGroup
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.props.KtorOptionGroup
import io.github.xzima.docomagos.server.props.KtorProps
import io.github.xzima.docomagos.server.props.RsocketOptionGroup
import io.github.xzima.docomagos.server.props.RsocketProps
import io.github.xzima.docomagos.server.routes.RouteInjector
import io.github.xzima.docomagos.server.routes.RsocketRouteInjector
import io.github.xzima.docomagos.server.routes.StaticUiRouteInjector
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.JobService
import io.github.xzima.docomagos.server.services.PingService
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.github.xzima.docomagos.server.services.impl.GitServiceImpl
import io.github.xzima.docomagos.server.services.impl.JobServiceImpl
import io.github.xzima.docomagos.server.services.impl.PingServiceImpl
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

    private val loggingLevel: Level by option(valueSourceKey = "logging-level").enum<Level>().required()
    private val rsocketProps: RsocketProps by RsocketOptionGroup()
    private val ktorProps: KtorProps by KtorOptionGroup()
    private val appProps: AppProps by AppOptionGroup()
    private val gitProps: GitProps by GitOptionGroup()

    override fun run(): Unit = runBlocking {
        KotlinLogging.configureLogging(loggingLevel)
        initKoinModule()
        // TODO create check external services task
        inject<DockerComposeService>().listProjects().also {
            logger.debug { "DC: ${it.size}" }
        }
    }

    fun initKoinModule() = configureKoin {
        single { rsocketProps }
        single { ktorProps }
        single { appProps }
        single { gitProps }
        single { DockerComposeService() }
        single<GitClient> { GitClientImpl(get<GitProps>().gitAskPass) }
        single<GitService> { GitServiceImpl(get<GitProps>(), get<GitClient>()) }
        single<PingService> { PingServiceImpl() }
        single<JobService> { JobServiceImpl(get<AppProps>(), get<PingService>(), get<GitService>()) }
        // routes
        single { StaticUiRouteInjector(get<AppProps>()) } bind RouteInjector::class
        single { RsocketRouteInjector(get<ListProjectsHandler>()) } bind RouteInjector::class
        // handlers
        single { ListProjectsHandler(get<DockerComposeService>()) }
    }
}
