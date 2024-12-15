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
            api(kotlinxCoroutines.kotlinxCoroutinesCore)

            api(libs.rsocket.core)
            api(kotlinxSerialization.kotlinxSerializationProtobuf)

            api(libs.logging)
            api(libs.kotlinx.datetime)
        }
    }
}
