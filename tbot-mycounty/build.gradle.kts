@file:Suppress("UnstableApiUsage")

import java.time.Instant

plugins {
    kotlin("jvm")
    application
}

version = "3.1.0-SNAPSHOT"

val javaVersion: String by project
java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)

application {
    mainClass = "me.alllex.tbot.mycounty.Main"
    applicationDefaultJvmArgs = listOf("-Dlog4j.shutdownHookEnabled=false")
}

distributions.main {
    contents.into("/${project.name}")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.tbotApi)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.java)
    implementation(libs.bundles.log4j)
    implementation(libs.bundles.jackson.kotlin.yaml)
    implementation(libs.sqlite.jdbc)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.assertk.jvm)

    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.ktor.serialization.kotlinx.json)
}

tasks.test {
    useJUnitPlatform()
}

kotlin.compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
}

val createBuildProperties by tasks.registering {
    val projectVersion = version
    val outputDir = layout.buildDirectory.file("build-properties")

    inputs.property("version", projectVersion)
    outputs.dir(outputDir)

    doLast {
        outputDir.get().asFile
            .apply { mkdirs() }
            .resolve("build.properties")
            .writeText(
                """
                    version=$projectVersion
                    time=${Instant.ofEpochMilli(System.currentTimeMillis())}
                """.trimIndent()
            )
    }
}

sourceSets.main {
    resources.srcDir(createBuildProperties)
}

tasks.processResources {
    dependsOn(createBuildProperties)
}
