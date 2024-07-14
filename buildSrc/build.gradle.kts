plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.17.1")
    implementation("me.alllex.parsus:parsus-jvm:0.6.1")
    implementation(libs.kotlinx.serialization.json)
}

