/**
 * Copyright 2024-2025 Alex Zima(xzima@ro.ru)
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
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.mock)
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
            implementation(ktor.ktorNetwork)

            implementation(libs.okio)

            implementation(libs.rsocket.ktor.server)
            implementation(libs.rsocket.ktor.ws.server)

            implementation(libs.kommand)
            implementation(libs.clikt)
            implementation(kotlinxSerialization.kotlinxSerializationJson)
            implementation(libs.kaml)
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
