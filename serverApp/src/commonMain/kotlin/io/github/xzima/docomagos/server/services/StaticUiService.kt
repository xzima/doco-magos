package io.github.xzima.docomagos.server.services

import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.ext.static_router.StaticRouter
import io.ktor.server.routing.*
import okio.Path.Companion.toPath

class StaticUiService(env: AppEnv) : StaticRouter(env.staticUiPath.toPath()) {

    fun configureStaticUi(route: Route) = route.apply {
        default("index.html")
        files(".")
    }
}
