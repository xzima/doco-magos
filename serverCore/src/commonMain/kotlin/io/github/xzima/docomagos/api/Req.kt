package io.github.xzima.docomagos.api

import kotlinx.serialization.*

@Serializable
sealed interface Req<out RS> {
    companion object {
        val polymorphicSerializer = serializer(
            PolymorphicSerializer(Any::class),
        )
    }

    @Serializable
    data object ListProjects : Req<ListProjectsResp>
}
