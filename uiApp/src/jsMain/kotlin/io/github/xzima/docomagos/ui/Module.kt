package io.github.xzima.docomagos.ui

import io.github.xzima.docomagos.client.DockerComposeApiService
import io.github.xzima.docomagos.client.DockerComposeApiServiceImpl
import io.github.xzima.docomagos.ui.states.DCProjectsListViewModel
import io.rsocket.kotlin.RSocket
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoinModule(client: RSocket) = startKoin {
//    logger(//TODO)
    modules(
        module {
            single<DockerComposeApiService> { DockerComposeApiServiceImpl(client) }
            factory { DCProjectsListViewModel(get()) }
        },
    )
}
