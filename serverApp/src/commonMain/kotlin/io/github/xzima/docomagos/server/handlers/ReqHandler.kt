package io.github.xzima.docomagos.server.handlers

import io.github.xzima.docomagos.api.Req

interface ReqHandler<RQ : Req<RS>, RS> {
    suspend fun handle(request: RQ): RS
}
