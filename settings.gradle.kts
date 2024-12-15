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
