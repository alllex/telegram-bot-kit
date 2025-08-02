@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion


plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinx.binaryCompatibilityValidator)
    `maven-publish`
    signing
    alias(libs.plugins.dokka)
    alias(libs.plugins.nexus.publish)
}

repositories {
    mavenCentral()
}

val publishVersion = layout.projectDirectory.file("version.txt").asFile.readText().trim()

group = "me.alllex.telegram.botkit"
version = publishVersion

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

val updateApiSpec by tasks.registering {
    group = "telegram"
    val specFile = layout.projectDirectory.file("api-spec/telegram-bot-api.html")
    outputs.file(specFile)
    outputs.upToDateWhen { false }
    doLast {
        val url = "https://core.telegram.org/bots/api"
        val rawText = uri(url).toURL().readText()
        val genTimeRe = "^<!-- page generated in .* -->$".toRegex()
        val text = rawText.lineSequence()
            .filterNot { genTimeRe.matches(it) }
            .joinToString("\n")
        specFile.asFile.writeText(text)
    }
}

val extractBotApi by tasks.registering(BotApiExtract::class) {
    group = "telegram"
    apiSpecFile = layout.projectDirectory.file("api-spec/telegram-bot-api.html")
    apiArgListFile = layout.projectDirectory.file("api-spec/telegram-bot-api.args.json")
    outputFile = layout.projectDirectory.file("api-spec/telegram-bot-api.json")

    mustRunAfter(updateApiSpec)
}

val generateBotApi by tasks.registering(BotApiGenerate::class) {
    group = "telegram"
    apiJsonFile = extractBotApi.flatMap { it.outputFile }
    packageName = "me.alllex.tbot.api.model"
    telegramClientPackage = "me.alllex.tbot.api.client"
    outputDirectory = layout.projectDirectory.dir("src/main/generated-kotlin")
}

tasks.named("apiDump") { mustRunAfter(generateBotApi) }
tasks.named("apiCheck") { mustRunAfter("apiDump") }

val extractBotApiArgLists by tasks.registering(BotApiArgListExtract::class) {
    group = "telegram"
    apiJsonFile = extractBotApi.flatMap { it.outputFile }
    outputFile = layout.projectDirectory.file("api-spec/telegram-bot-api.args.json")

    mustRunAfter(generateBotApi)
}

tasks.register("rollNextApi") {
    dependsOn(updateApiSpec, generateBotApi, extractBotApiArgLists, "apiDump")
}

kotlin.sourceSets.main {
    kotlin.srcDir(generateBotApi)
}

kotlin.compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
}

val sourcesJar by tasks.registering(Jar::class) {
    this.archiveClassifier = "sources"
    from(sourceSets.main.map { it.allSource })
}

val javadocJar by tasks.registering(Jar::class) {
    description = "Produce javadoc with Dokka HTML inside"
    from(tasks.dokkaGeneratePublicationHtml)
    archiveClassifier = "javadoc"
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = "tbot-api-jvm"
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}

// Gradle hasn't updated the signing plugin to be compatible with lazy-configuration, so it needs weird workarounds:
afterEvaluate {
    // Register signatures in afterEvaluate, otherwise the signing plugin creates the signing tasks
    // too early, before all the publications are added.
    signing {
        val signingKeyId: String? by project
        val signingKey: String? by project
        val signingPassword: String? by project

        if (signingKeyId != null) {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name = "Telegram BotKit"
            description = "Fluent Kotlin bindings for Telegram Bot API"
            url = "https://github.com/alllex/telegram-bot-kit"
            licenses {
                license {
                    name = "MIT"
                    url = "https://opensource.org/licenses/MIT"
                }
            }
            developers {
                developer {
                    id = "alllex"
                    name = "Alex by Software"
                    email = "software@alllex.me"
                    url = "https://alllex.me"
                }
            }
            scm {
                connection = "scm:git:git@github.com:alllex/telegram-bot-kit.git"
                developerConnection = "scm:git:git@github.com:alllex/telegram-bot-kit.git"
                url = "https://github.com/alllex/telegram-bot-kit"
            }
        }
    }
}


nexusPublishing {
    repositories {
        sonatype {
            nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
            snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

