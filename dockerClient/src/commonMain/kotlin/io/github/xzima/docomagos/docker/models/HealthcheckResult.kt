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
import kotlin.time.ExperimentalTime

/**
 * HealthcheckResult stores information about a single run of a healthcheck probe
 *
 * @param start Date and time at which this check started in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
 * @param end Date and time at which this check ended in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
 * @param exitCode ExitCode meanings:  - `0` healthy - `1` unhealthy - `2` reserved (considered unhealthy) - other values: error running probe
 * @param output Output from last check
 */
@Serializable
data class HealthcheckResult
    @ExperimentalTime
    constructor(
        // Date and time at which this check started in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
        @SerialName(value = "Start") val start: kotlin.time.Instant? = null,
        // Date and time at which this check ended in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
        @SerialName(value = "End") val end: String? = null,
        // ExitCode meanings:  - `0` healthy - `1` unhealthy - `2` reserved (considered unhealthy) - other values: error running probe
        @SerialName(value = "ExitCode") val exitCode: Int? = null,
        // Output from last check
        @SerialName(value = "Output") val output: String? = null,
    )
