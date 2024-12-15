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
package io.github.xzima.docomagos.server.ext.static_router

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import okio.*

open class StaticRouter(
    private val staticRootFolder: Path,
) {

    protected fun Route.files(folder: String) {
        val dir = staticRootFolder.resolve(folder)
        val pathParameter = "static-content-path-parameter"
        get("{$pathParameter...}") {
            val relativePath = call.parameters.getAll(pathParameter)
                ?.joinToString(Path.DIRECTORY_SEPARATOR)
                ?: return@get
            val file = dir.resolve(relativePath)
            call.respondStatic(file)
        }
    }

    protected fun Route.default(localPath: String) {
        val path = staticRootFolder.resolve(localPath)
        get {
            call.respondStatic(path)
        }
    }

    protected fun Route.file(remotePath: String, localPath: String) {
        val path = staticRootFolder.resolve(localPath)

        get(remotePath) {
            call.respondStatic(path)
        }
    }

    private suspend inline fun ApplicationCall.respondStatic(path: Path) {
        if (FileSystem.SYSTEM.exists(path)) {
            respond(LocalFileContent(path))
        }
    }
}
