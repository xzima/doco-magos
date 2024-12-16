/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
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

import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.ext.staticrouter.StaticRouter
import io.ktor.server.routing.*
import okio.Path.Companion.toPath

class StaticUiService(
    env: AppEnv,
) : StaticRouter(env.staticUiPath.toPath()) {

    fun configureStaticUi(route: Route) = route.apply {
        default("index.html")
        files(".")
    }
}
