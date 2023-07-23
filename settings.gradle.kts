pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm").version(kotlinVersion).apply(false)
        kotlin("plugin.serialization").version(kotlinVersion).apply(false)
    }
}

plugins {
    `gradle-enterprise`
}

rootProject.name = "telegram-bot-kit"

include("tbot-api")
include("tbot-echo")
include("tbot-mycounty")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(System.getenv("CI") == "true")
    }
}

dependencyResolutionManagement {
    val kotlinVersion: String by settings

    versionCatalogs {
        create("libs") {
            fun lib(group: String, artifact: String, alias: String = artifact) = library(alias, group, artifact)

            val kotlin = version("kotlin", kotlinVersion)
            lib("org.jetbrains.kotlin", "kotlin-bom").versionRef(kotlin)
            lib("org.jetbrains.kotlin", "kotlin-reflect").versionRef(kotlin)
            lib("org.jetbrains.kotlin", "kotlin-test-junit5").versionRef(kotlin)

            val coroutines = version("kotlinx-coroutines", "1.6.4")
            lib("org.jetbrains.kotlinx", "kotlinx-coroutines-bom").versionRef(coroutines)
            lib("org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef(coroutines)
            lib("org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef(coroutines)

            val serialization = version("kotlinx-serialization", "1.5.1")
            lib("org.jetbrains.kotlinx", "kotlinx-serialization-bom").versionRef(serialization)
            lib("org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef(serialization)

            val ktor = version("ktor", "2.3.2")
            lib("io.ktor", "ktor-client-core").versionRef(ktor)
            lib("io.ktor", "ktor-client-logging").versionRef(ktor)
            lib("io.ktor", "ktor-client-java").versionRef(ktor)
            lib("io.ktor", "ktor-client-mock").versionRef(ktor)
            lib("io.ktor", "ktor-client-content-negotiation").versionRef(ktor)
            lib("io.ktor", "ktor-serialization-kotlinx-json").versionRef(ktor)

            val slf4j = version("slf4j", "1.7.36")
            lib("org.slf4j", "slf4j-api").versionRef(slf4j)
            lib("org.slf4j", "slf4j-simple").versionRef(slf4j)

            val log4j = version("log4j", "2.20.0")
            lib("org.apache.logging.log4j", "log4j-bom").versionRef(log4j)
            lib("org.apache.logging.log4j", "log4j-core").versionRef(log4j)
            lib("org.apache.logging.log4j", "log4j-api").versionRef(log4j)
            lib("org.apache.logging.log4j", "log4j-slf4j2-impl").versionRef(log4j)
            bundle("log4j", listOf("log4j-core", "log4j-api", "log4j-slf4j2-impl"))

            val junit = version("junit5", "5.9.2")
            lib("org.junit", "junit-bom").versionRef(junit)
            lib("org.junit.jupiter", "junit-jupiter").versionRef(junit)

            val assertk = version("assertk", "0.26.1")
            lib("com.willowtreeapps.assertk", "assertk-jvm").versionRef(assertk)

            val jackson = version("jackson", "2.15.1")
            lib("com.fasterxml.jackson.core", "jackson-annotations").versionRef(jackson)
            lib("com.fasterxml.jackson.core", "jackson-databind").versionRef(jackson)
            lib("com.fasterxml.jackson.module", "jackson-module-kotlin").versionRef(jackson)
            lib("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml").versionRef(jackson)
            bundle("jackson-kotlin-yaml", listOf("jackson-annotations", "jackson-databind", "jackson-module-kotlin", "jackson-dataformat-yaml"))

            val sqlite = version("sqlite", "3.40.1.0")
            lib("org.xerial", "sqlite-jdbc").versionRef(sqlite)
        }
    }
}
