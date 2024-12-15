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
