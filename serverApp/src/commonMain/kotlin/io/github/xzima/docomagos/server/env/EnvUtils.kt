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
package io.github.xzima.docomagos.server.env

import com.kgit2.kommand.env.envVar

object EnvUtils {

    fun getEnvVar(name: String): String {
        return checkNotNull(findEnvVar(name)) { "Environment variable $name should not be blank" }
    }

    fun <T> getEnvVar(name: String, transform: (String) -> T): T {
        val stringValue = getEnvVar(name)
        return try {
            transform(stringValue)
        } catch (e: Exception) {
            throw IllegalStateException("Environment variable $name=${stringValue} transformation failed", e)
        }
    }

    fun findEnvVar(name: String): String? {
        return envVar(name)?.takeIf { it.isNotBlank() }
    }

    fun <T> findEnvVar(name: String, transform: (String) -> T?): T? {
        val stringValue = findEnvVar(name) ?: return null
        return try {
            transform(stringValue)
        } catch (e: Exception) {
            throw IllegalStateException("Environment variable $name=${stringValue} transformation failed", e)
        }
    }
}
