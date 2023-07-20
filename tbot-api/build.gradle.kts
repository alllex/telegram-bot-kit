@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

group = "me.alllex.tbot.api"
version = "0.0.1"

val javaVersion: String by project
java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.slf4j.api)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.assertk.jvm)
    testRuntimeOnly(libs.slf4j.simple)
}

tasks.test {
    useJUnitPlatform()
}

val generateTelegramBotApi by tasks.registering(GenerateTelegramBotApiTask::class) {
    apiSpecFile = layout.projectDirectory.file("../api-spec/telegram-bot-api.html")
    packageName = "me.alllex.tbot.api.model"
    telegramClientPackage = "me.alllex.tbot.api.client"
    outputDirectory = layout.buildDirectory.dir("generated-api-sources")
}

kotlin.sourceSets.main {
    kotlin.srcDir(generateTelegramBotApi)
}

kotlin.compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
}
