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
import dev.aga.gradle.versioncatalogs.Generator.generate
import dev.aga.gradle.versioncatalogs.GeneratorConfig
import org.tomlj.Toml

rootProject.name = "doco-magos"

plugins {
    id("dev.aga.gradle.version-catalog-generator") version ("2.0.0")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }

    val bomPrefix = "bom-"
    val tomlPath = "gradle/libs.versions.toml"
    val bomList = file(tomlPath).reader().use { reader ->
        val libraries = Toml.parse(reader).getTableOrEmpty("libraries")
        libraries.keySet().filter { it.startsWith(bomPrefix) }
    }

    versionCatalogs {
        bomList.forEach { name ->
            generate(name.removePrefix(bomPrefix)) {
                from(toml(name))
                aliasPrefixGenerator = GeneratorConfig.NO_PREFIX
            }
        }
    }
}

include(":serverApp")
include(":serverClient")
include(":serverCore")
include(":uiApp")
include(":uiAppMock")
include(":uiCore")
