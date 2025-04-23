plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.plugin.serialization") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.19.1")
    implementation("me.alllex.parsus:parsus-jvm:0.6.1")
    implementation(libs.kotlinx.serialization.json)
}

