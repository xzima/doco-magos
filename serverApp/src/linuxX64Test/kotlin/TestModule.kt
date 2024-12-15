import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.StaticUiService
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initTestKoinModule(port: Int) = startKoin {
//    logger(//TODO)
    modules(
        module {
            single {
                RSocketEnv(
                    1024,
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
                    "/static/ui/path",
                )
            }
            single { StaticUiService(get()) }
            single { DockerComposeService() }
            // handlers
            single { ListProjectsHandler(get()) }
        },
    )
}
