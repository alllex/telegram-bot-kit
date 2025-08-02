plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.plugin.serialization") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jsoup)
    implementation(libs.parsus.jvm)
    implementation(libs.kotlinx.serialization.json)
}

