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
 * The logging configuration for this container
 *
 * @param type
 * @param config
 */
@Serializable
data class HostConfigAllOfLogConfig(
    @SerialName(value = "Type") val type: Type? = null,
    @SerialName(value = "Config") val config: Map<String, String>? = null,
) {

    /**
     *
     *
     * Values: jsonMinusFile,syslog,journald,gelf,fluentd,awslogs,splunk,etwlogs,none
     */
    @Serializable
    enum class Type(
        val value: String,
    ) {
        @SerialName(value = "json-file")
        JSON_MINUS_FILE("json-file"),

        @SerialName(value = "syslog")
        SYSLOG("syslog"),

        @SerialName(value = "journald")
        JOURNALD("journald"),

        @SerialName(value = "gelf")
        GELF("gelf"),

        @SerialName(value = "fluentd")
        FLUENTD("fluentd"),

        @SerialName(value = "awslogs")
        AWSLOGS("awslogs"),

        @SerialName(value = "splunk")
        SPLUNK("splunk"),

        @SerialName(value = "etwlogs")
        ETWLOGS("etwlogs"),

        @SerialName(value = "none")
        NONE("none"),
    }
}
