@file:Suppress("UnstableApiUsage")

import java.time.Instant

plugins {
    kotlin("jvm")
    application
}

version = "2.2.2"

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
    implementation(projects.tbotBot)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.bundles.log4j)
    implementation(libs.bundles.jackson.kotlin.yaml)
    implementation(libs.sqlite.jdbc)
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
