package io.github.xzima.docomagos.server.services.models

import kotlinx.serialization.*

@Serializable
data class DCListProjects(
    @SerialName("Name")
    val name: String,
    @SerialName("Status")
    val status: String,
    @SerialName("ConfigFiles")
    val configFiles: String,
)
