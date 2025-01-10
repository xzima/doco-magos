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
package io.github.xzima.docomagos.server.routes

import io.github.xzima.docomagos.server.ext.staticrouter.StaticRouter
import io.github.xzima.docomagos.server.props.AppProps
import io.ktor.server.routing.*
import okio.Path.Companion.toPath

class StaticUiRouteInjector(
    env: AppProps,
) : StaticRouter(env.staticUiPath.toPath()),
    RouteInjector {

    override fun injectRoutes(routing: Routing): Unit = routing.run {
        route("/") {
            default("index.html")
            files(".")
        }
    }
}
