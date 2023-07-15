plugins {
    kotlin("jvm")
    application
}

version = "2.2.2"

val javaVersion: String by project
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

application {
    mainClass.set("me.alllex.tbot.mycounty.Main")
    applicationDefaultJvmArgs = listOf("-Dlog4j.shutdownHookEnabled=false")
}

distributions {
    main {
        contents {
            into("/${project.name}")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.tbotApi)
    implementation(projects.tbotBot)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.log4j)
    implementation(libs.bundles.jackson.kotlin.yaml)
    implementation(libs.sqlite.jdbc)
}

kotlin.compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
}

