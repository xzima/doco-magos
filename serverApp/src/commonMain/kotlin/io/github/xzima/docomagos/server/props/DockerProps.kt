package io.github.xzima.docomagos.server.props

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import io.ktor.client.plugins.logging.*

interface DockerProps {
    val loggingLevel: LogLevel
    val unixSocketFile: String
}

class DockerOptionGroup :
    OptionGroup(),
    DockerProps {
    override val loggingLevel: LogLevel by option(valueSourceKey = "docker.logging-level").enum<LogLevel>().required()
    override val unixSocketFile: String by option(valueSourceKey = "docker.unix-socket-file").required()
}
