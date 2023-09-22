@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion


plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinx.binaryCompatibilityValidator)
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "me.alllex.telegram.botkit"
version = "0.3.0-SNAPSHOT"

java.toolchain.languageVersion = libs.versions.jvmToolchain.map { JavaLanguageVersion.of(it) }

tasks.withType<JavaCompile>().configureEach {
    options.release = libs.versions.jdkTarget.map { it.toInt() }
}

kotlin {
    val kotlinTarget = libs.versions.kotlinTarget
    val kotlinVersion = kotlinTarget.map { KotlinVersion.fromVersion(it.toKotlinMinor()) }
    compilerOptions {
        languageVersion = kotlinVersion
        apiVersion = kotlinVersion
    }
    coreLibrariesVersion = kotlinTarget.get()

    compilerOptions {
        jvmTarget = libs.versions.jdkTarget.map { JvmTarget.fromTarget(it.toJdkTarget()) }
        // https://kotlinlang.org/docs/compiler-reference.html#xjdk-release-version
        freeCompilerArgs.add(libs.versions.jdkTarget.map { "-Xjdk-release=${it.toJdkTarget()}" })
    }
}

// 8 => 1.8, 11 => 11
fun String.toJdkTarget() = if (toInt() <= 8) "1.$this" else this

// 1.7.21 => 1.7, 1.9 => 1.9
fun String.toKotlinMinor() = split(".").take(2).joinToString(".")

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.slf4j.api)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.assertk.jvm)
    testRuntimeOnly(libs.bundles.log4j)

    // demo
    testImplementation(libs.ktor.client.java)
}

tasks.test {
    useJUnitPlatform()
}

val generateTelegramBotApi by tasks.registering(GenerateTelegramBotApiTask::class) {
    apiSpecFile = layout.projectDirectory.file("../api-spec/telegram-bot-api.html")
    packageName = "me.alllex.tbot.api.model"
    telegramClientPackage = "me.alllex.tbot.api.client"
    outputDirectory = layout.projectDirectory.dir("src/main/generated-kotlin")
}

kotlin.sourceSets.main {
    kotlin.srcDir(generateTelegramBotApi)
}

kotlin.compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
}

val sourcesJar by tasks.registering(Jar::class) {
    this.archiveClassifier = "sources"
    from(sourceSets.main.map { it.allSource })
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
