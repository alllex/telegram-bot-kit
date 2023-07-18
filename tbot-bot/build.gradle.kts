@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

group = "me.alllex.tbot.bot"
version = "0.0.1"

val javaVersion: String by project
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

dependencies {
    api(projects.tbotApi)

    implementation(libs.bundles.log4j)

    testImplementation(libs.kotlin.test.junit5)
}

tasks.test {
    useJUnitPlatform()
}

kotlin.compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
}
