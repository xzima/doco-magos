package io.github.xzima.docomagos.docker.ktor.engine.socket

class FailToConnectException : Exception("Connect timed out or retry attempts exceeded")
