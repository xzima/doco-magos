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
