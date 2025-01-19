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
package io.github.xzima.docomagos.docker.models

import kotlinx.serialization.*

/**
 * The behavior to apply when the container exits. The default is not to restart.  An ever increasing delay (double the previous delay, starting at 100ms) is added before each restart to prevent flooding the server.
 *
 * @param name - Empty string means not to restart - `no` Do not automatically restart - `always` Always restart - `unless-stopped` Restart always except when the user has manually stopped the container - `on-failure` Restart only when the container exit code is non-zero
 * @param maximumRetryCount If `on-failure` is used, the number of times to retry before giving up.
 */
@Serializable
data class RestartPolicy(
    // - Empty string means not to restart - `no` Do not automatically restart - `always` Always restart - `unless-stopped` Restart always except when the user has manually stopped the container - `on-failure` Restart only when the container exit code is non-zero
    @SerialName(value = "Name") val name: Name? = null,
    // If `on-failure` is used, the number of times to retry before giving up.
    @SerialName(value = "MaximumRetryCount") val maximumRetryCount: Int? = null,
) {

    /**
     * - Empty string means not to restart - `no` Do not automatically restart - `always` Always restart - `unless-stopped` Restart always except when the user has manually stopped the container - `on-failure` Restart only when the container exit code is non-zero
     *
     * Values: ,no,always,unlessMinusStopped,onMinusFailure
     */
    @Serializable
    enum class Name(
        val value: String,
    ) {
        @SerialName(value = "")
        UNKNOWN(""),

        @SerialName(value = "no")
        NO("no"),

        @SerialName(value = "always")
        ALWAYS("always"),

        @SerialName(value = "unless-stopped")
        UNLESS_MINUS_STOPPED("unless-stopped"),

        @SerialName(value = "on-failure")
        ON_MINUS_FAILURE("on-failure"),
    }
}
