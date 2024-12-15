plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    // Other supported targets are listed here: https://ktor.io/docs/native-server.html#targets
    linuxX64 {
        binaries {
            executable {
                entryPoint = "io.github.xzima.docomagos.server.main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.serverCore)

            implementation(ktor.ktorServerCore)
            implementation(ktor.ktorServerCio)
            implementation(ktor.ktorServerContentNegotiation)
            implementation(ktor.ktorServerCallId)

            implementation(libs.okio)

            implementation(libs.rsocket.ktor.server)
            implementation(libs.rsocket.ktor.ws.server)

            implementation(koin.koinCore)

            implementation(libs.kommand)
            implementation(kotlinxSerialization.kotlinxSerializationJson)
        }

        linuxX64Main.dependencies { }

        linuxX64Test.dependencies {
            implementation(projects.serverClient)
            implementation(ktor.ktorClientCio)

            implementation(kotlinLibs.kotlinTest)

            implementation(kotest.kotestAssertionsCore)
            implementation(kotest.kotestAssertionsJson)
            implementation(libs.kotest.assertions.ktor)
        }
    }
}
