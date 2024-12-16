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
package io.github.xzima.docomagos.server.ext.staticrouter

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import okio.*

class LocalFileContent(
    private val path: Path,
) : OutgoingContent.WriteChannelContent() {

    override val contentLength: Long get() = stat().size ?: -1

    override val contentType: ContentType = extractContentType(path)

    override suspend fun writeTo(channel: ByteWriteChannel) {
        val source = FileSystem.SYSTEM.source(path)
        source.use { fileSource ->
            fileSource.buffer().use { bufferedFileSource ->
                val buf = ByteArray(4 * 1024)
                while (true) {
                    val read = bufferedFileSource.read(buf)
                    if (read <= 0) break
                    channel.writeFully(buf, 0, read)
                }
            }
        }
    }

    init {
        if (!FileSystem.SYSTEM.exists(path)) {
            throw IllegalStateException("No such file ${path.normalized()}")
        }

        stat().lastModifiedAtMillis?.let {
            versions += LastModifiedVersion(GMTDate(it))
        }
    }

    private fun stat(): FileMetadata = FileSystem.SYSTEM.metadata(path)

    private fun extractContentType(path: Path): ContentType {
        val contentTypes = ContentType.fromFilePath(path.name)
        val contentType = contentTypes.firstOrNull() ?: ContentType.Application.OctetStream
        return when {
            null == contentType.charset() -> contentType.withCharsetIfNeeded(Charsets.UTF_8)

            else -> contentType
        }
    }
}
