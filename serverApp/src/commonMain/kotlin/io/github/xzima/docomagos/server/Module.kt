package io.github.xzima.docomagos.server

import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.env.EnvUtils
import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.StaticUiService
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

inline fun <reified T : Any> inject() = KoinPlatform.getKoin().get<T>()

fun initKoinModule() = startKoin {
//    logger(//TODO)
    modules(
        module {
            single {
                RSocketEnv(
                    EnvUtils.getEnvVar("RSOCKET_MAX_FRAGMENT_SIZE") { it.toInt() },
                )
            }
            single {
                KtorEnv(
                    EnvUtils.getEnvVar("KTOR_PORT") { it.toInt() },
                    EnvUtils.getEnvVar("KTOR_REUSE_ADDRESS") { it.toBoolean() },
                    EnvUtils.getEnvVar("KTOR_GRACE_PERIOD_MILLIS") { it.toLong() },
                    EnvUtils.getEnvVar("KTOR_GRACE_TIMEOUT_MILLIS") { it.toLong() },
                )
            }
            single {
                AppEnv(
                    EnvUtils.getEnvVar("STATIC_UI_PATH"),
                )
            }
            single { StaticUiService(get()) }
            single { DockerComposeService() }
            // handlers
            single { ListProjectsHandler(get()) }
        },
    )
}
