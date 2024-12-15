package io.github.xzima.docomagos.api

import kotlinx.serialization.*

@Serializable
data class ListProjectsResp(
    val projects: List<ProjectItem>,
) {

    @Serializable
    data class ProjectItem(
        val name: String,
        val status: String,
    )
}
