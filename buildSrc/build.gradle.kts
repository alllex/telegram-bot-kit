plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("me.alllex.parsus:parsus-jvm:0.5.5")
}

val updateApiSpec by tasks.registering {
    val specFile = layout.projectDirectory.file("../api-spec/telegram-bot-api.html")
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
