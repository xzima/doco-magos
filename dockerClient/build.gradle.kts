/**
 * Copyright 2025 Alex Zima(xzima@ro.ru)
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
}

kotlin {
    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.serverCore)

                implementation(ktor.ktorClientContentNegotiation)
                implementation(ktor.ktorSerializationKotlinxJson)
                implementation(kotlinxSerialization.kotlinxSerializationJson)
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
//
//                api("io.ktor:ktor-client-core:$ktor_version")
//                api("io.ktor:ktor-client-serialization:$ktor_version")
//                api("io.ktor:ktor-client-content-negotiation:$ktor_version")
//                api("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
//
//                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
    }
}
