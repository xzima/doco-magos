/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
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
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.mock)
}

kotlin {
    js {
        browser()
    }

    sourceSets {

        commonMain.dependencies {
            api(projects.serverCore)

            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            api(compose.ui)
            api(compose.components.resources)

            api(libs.ui.lifecycle.viewmodel)
            api(libs.ui.lifecycle.compose)
            api(libs.ui.highlights)
            api(libs.ui.routing)

            api(koin.koinCompose)
            api(koin.koinComposeViewmodel)
        }
    }
}
