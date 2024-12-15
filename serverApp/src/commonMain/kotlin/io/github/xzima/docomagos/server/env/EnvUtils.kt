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
