@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("jvm")
    application
}

group = "me.alllex.tbot.echobot"
version = "0.0.1"

val javaVersion: String by project
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.tbotBot)

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)

}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation(libs.junit.jupiter)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

application {
    mainClass = "me.alllex.tbot.echobot.EchoBotKt"
}
