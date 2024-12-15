plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    js {
        browser()
    }
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            api(projects.serverCore)

            api(ktor.ktorClientCore)
            api(ktor.ktorClientEncoding)
            api(ktor.ktorClientLogging)

            api(libs.rsocket.ktor.client)
            api(libs.rsocket.ktor.ws.client)
        }
    }
}
